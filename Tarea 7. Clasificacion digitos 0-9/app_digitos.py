"""
Clasificador de dígitos escritos a mano (0-9) en tiempo real.

Como ejecutarlo
Desde la terminal, estar dentro de la direccion del programa y usar el comando py -3.12 app_digitos.py

Qué hace este programa:
  1. Carga el modelo MLP que ya entrenamos en el notebook (archivo .keras).
  2. Abre una ventana con un lienzo negro donde dibujas con el mouse.
  3. Convierte tu dibujo a una imagen de 28x28 píxeles, igual a las de MNIST,
     porque el modelo solo sabe leer imágenes con ese formato.
  4. Se la da al modelo, que responde con 10 probabilidades (una por dígito).
     El dígito con la probabilidad más alta es la predicción.

"""
import os
os.environ["TF_CPP_MIN_LOG_LEVEL"] = "2"  # menos mensajes de TensorFlow en consola (va antes de importarlo)

import tkinter as tk                  # librería para crear ventanas (viene con Python)
from tkinter import ttk, messagebox   # ttk: botones y barras con mejor aspecto; messagebox: ventanas de aviso
from pathlib import Path              # para manejar rutas de archivos

import numpy as np                    # arreglos numéricos: una imagen es una matriz de números
from PIL import Image, ImageDraw      # Pillow: crear y dibujar imágenes en memoria
import tensorflow as tf               # para cargar el modelo y hacer la predicción

# El modelo debe estar en la misma carpeta que este archivo
MODEL_PATH = Path(__file__).with_name("modelo_mnist_25.keras")
CANVAS_SIZE = 280   # 10 veces el tamaño de MNIST (28x28)
BRUSH = 26          # grosor del pincel


def preparar_imagen(img):
    """Convierte el dibujo (PIL, 'L', blanco sobre negro) al formato de MNIST:
    28x28, dígito de ~20 px centrado por su centro de masa, valores 0-1.

    Esto es necesario porque el modelo aprendió con dígitos blancos sobre fondo
    negro, de tamaño parecido y centrados. Si le damos el dibujo sin ajustar,
    acierta mucho menos."""
    arr = np.array(img)   # imagen -> matriz de números (0 = negro, 255 = blanco)
    if arr.max() == 0:
        return None  # lienzo vacío: no hay nada dibujado

    # 1) Recortar a la caja que contiene el dígito
    ys, xs = np.where(arr > 0)   # filas y columnas de todos los píxeles pintados
    recorte = img.crop((xs.min(), ys.min(), xs.max() + 1, ys.max() + 1))

    # 2) Escalar para que el lado mayor mida 20 px 
    w, h = recorte.size
    escala = 20.0 / max(w, h)   # factor de reducción
    nuevo_w, nuevo_h = max(1, round(w * escala)), max(1, round(h * escala))
    recorte = recorte.resize((nuevo_w, nuevo_h), Image.LANCZOS)   # LANCZOS: reduce sin perder mucha calidad

    # 3) Centrar por centro de masa dentro de un lienzo de 28x28
    a = np.array(recorte, dtype=np.float32)
    total = a.sum()   # brillo total de la imagen
    # centro de masa: promedio de las posiciones, dando más peso a los píxeles más brillantes
    cy, cx = (np.indices(a.shape) * a).sum(axis=(1, 2)) / total
    # esquina donde hay que pegar el recorte para que su centro de masa caiga en (14, 14);
    # np.clip evita que el recorte se salga del lienzo
    ox = int(np.clip(round(14 - cx), 0, 28 - nuevo_w))
    oy = int(np.clip(round(14 - cy), 0, 28 - nuevo_h))

    lienzo = Image.new("L", (28, 28), 0)   # imagen de 28x28 toda negra ("L" = escala de grises)
    lienzo.paste(recorte, (ox, oy))
    # dividir entre 255 deja los valores entre 0 y 1, igual que en el notebook (gray_scale = 255)
    return np.array(lienzo, dtype=np.float32) / 255.0


class App:
    def __init__(self, root):
        """Carga el modelo y construye la ventana."""
        self.root = root
        root.title("Clasificador de dígitos (MLP + MNIST)")

        # Si no está el archivo del modelo, avisa y cierra la ventana
        if not MODEL_PATH.exists():
            messagebox.showerror(
                "Falta el modelo",
                f"No encontré {MODEL_PATH.name} en la carpeta del programa.\n"
                "Ejecuta primero el notebook y guarda el modelo.")
            root.destroy()
            return
        # Carga el modelo ya entrenado (sin volver a entrenar nada)
        self.model = tf.keras.models.load_model(MODEL_PATH)

        # Imagen en memoria donde se dibuja lo mismo que se ve en pantalla.
        # El lienzo de tkinter solo sirve para mostrar; esta imagen es la que
        # se prepara y se le da al modelo.
        self.img = Image.new("L", (CANVAS_SIZE, CANVAS_SIZE), 0)
        self.draw = ImageDraw.Draw(self.img)
        self.ultimo = None   # último punto del trazo, para unir los puntos con una línea

        # Dos columnas: izquierda (lienzo y botones) y derecha (resultado)
        izquierda = ttk.Frame(root, padding=10)
        izquierda.grid(row=0, column=0)
        derecha = ttk.Frame(root, padding=10)
        derecha.grid(row=0, column=1, sticky="n")

        # Lienzo negro donde se dibuja
        self.canvas = tk.Canvas(izquierda, width=CANVAS_SIZE, height=CANVAS_SIZE,
                                bg="black", cursor="cross")
        self.canvas.pack()
        # Eventos del mouse: "<B1-Motion>" = se mueve con el clic izquierdo presionado,
        # "<ButtonRelease-1>" = se suelta el clic izquierdo
        self.canvas.bind("<B1-Motion>", self.pintar)
        self.canvas.bind("<ButtonRelease-1>", self.soltar)

        # Botones
        botones = ttk.Frame(izquierda)
        botones.pack(pady=8)
        ttk.Button(botones, text="Predecir", command=self.predecir).grid(row=0, column=0, padx=4)
        ttk.Button(botones, text="Borrar", command=self.borrar).grid(row=0, column=1, padx=4)

        # Número grande con la predicción y texto con la confianza
        self.resultado = ttk.Label(derecha, text="?", font=("Arial", 72, "bold"))
        self.resultado.pack()
        self.confianza = ttk.Label(derecha, text="Dibuja un dígito del 0 al 9")
        self.confianza.pack(pady=(0, 10))

        # Diez barras, una por dígito, que muestran la probabilidad de cada uno
        self.barras = []
        for d in range(10):
            fila = ttk.Frame(derecha)
            fila.pack(fill="x", pady=1)
            ttk.Label(fila, text=str(d), width=2).pack(side="left")
            barra = ttk.Progressbar(fila, length=160, maximum=100)
            barra.pack(side="left")
            self.barras.append(barra)

    def pintar(self, e):
        """Se ejecuta cada vez que mueves el mouse con el clic presionado.
        Dibuja dos veces lo mismo: en el lienzo (para que lo veas) y en la
        imagen en memoria (para dársela al modelo)."""
        r = BRUSH // 2   # radio del pincel
        self.canvas.create_oval(e.x - r, e.y - r, e.x + r, e.y + r, fill="white", outline="white")
        self.draw.ellipse([e.x - r, e.y - r, e.x + r, e.y + r], fill=255)
        if self.ultimo:  # une los puntos para que el trazo sea continuo
            x0, y0 = self.ultimo
            self.canvas.create_line(x0, y0, e.x, e.y, fill="white", width=BRUSH, capstyle="round")
            self.draw.line([x0, y0, e.x, e.y], fill=255, width=BRUSH)
        self.ultimo = (e.x, e.y)

    def soltar(self, _):
        """Se ejecuta al soltar el clic: termina el trazo y predice solo."""
        self.ultimo = None
        self.predecir()  # predice automáticamente al terminar cada trazo

    def borrar(self):
        """Limpia el lienzo, la imagen en memoria y el resultado."""
        self.canvas.delete("all")
        self.draw.rectangle([0, 0, CANVAS_SIZE, CANVAS_SIZE], fill=0)
        self.ultimo = None
        self.resultado.config(text="?")
        self.confianza.config(text="Dibuja un dígito del 0 al 9")
        for b in self.barras:
            b["value"] = 0

    def predecir(self):
        """Prepara el dibujo, se lo da al modelo y muestra la respuesta."""
        x = preparar_imagen(self.img)
        if x is None:
            return
        # reshape(1, 28, 28): el modelo espera un grupo de imágenes, aquí es un grupo de 1.
        # predict devuelve 10 probabilidades (suman 1); [0] toma las de nuestra única imagen.
        probs = self.model.predict(x.reshape(1, 28, 28), verbose=0)[0]
        digito = int(np.argmax(probs))   # posición de la probabilidad más alta = dígito predicho
        self.resultado.config(text=str(digito))
        self.confianza.config(text=f"Confianza: {probs[digito] * 100:.1f}%")
        for b, p in zip(self.barras, probs):
            b["value"] = float(p) * 100


# Este bloque solo corre al ejecutar el archivo directamente (py -3.12 app_digitos.py)
if __name__ == "__main__":
    root = tk.Tk()          # crea la ventana principal
    app = App(root)
    if root.winfo_exists():
        root.mainloop()     # mantiene la ventana abierta esperando eventos del mouse