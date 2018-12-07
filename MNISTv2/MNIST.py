APRENDE = True
DIBUJA = False

batch_size = 128
num_classes = 10
epochs = 20

import numpy as np #algebra lineal
import pandas as pd #Procesamiento de datos
import matplotlib.pyplot as plt #para dibujar
from collections import Counter
from sklearn.metrics import confusion_matrix
import json

import keras
from keras.datasets import mnist #base de datos de los digitos
from keras.models import Sequential
from keras.models import model_from_json
from keras.layers import Dense, Dropout, Flatten, Activation, BatchNormalization
from keras.preprocessing.image import ImageDataGenerator
from keras.callbacks import ReduceLROnPlateau

from datetime import datetime


print("Reconocimiento de dígitos MNIST: APRENDE = " + str(APRENDE) +
      ", DIBUJA = " + str(DIBUJA))

#Cargamos los datos
(x_train, y_train), (x_test, y_test) = mnist.load_data()


# Dibujar los 40 primeros numeros del conjunto de entrenamiento
if DIBUJA:
    labels = Counter(y_train)
    print(labels.items())

    plt.figure(figsize=(12,10))
    x, y = 10, 4
    for i in range(40):
    	plt.subplot(y,x,i+1)
    	plt.imshow(x_train[i].reshape((28,28)),interpolation = 'nearest')
    plt.show()

#Normalizamos los datos [0,255] => [0,1]
x_train = x_train /255.0
x_test = x_test /255.0

#Tamaño de los datasets
print('Tamaño x_train', x_train.shape)
print('Cantidad de muestras de entrenamiento', x_train.shape[0])
print('Cantidad de muestras de test', x_test.shape[0])

#Convertir los vectores de clases a matrices binarios One Hot Encoding
y_train = keras.utils.to_categorical(y_train, num_classes)

#Creamos el modelo de red neuronal
if APRENDE:

    model = Sequential()

    model.add(Dense(784, input_shape=(784,)))
    model.add(Activation('relu'))
    model.add(Dropout(0.2))


    model.add(Dense(10))
    model.add(Activation('softmax'))


    with open('model_architecture.json','w') as f:
        f.write(model.to_json())
    print("Model saved in model_architecture.json")

#Cargamos el modelo
else:
    with open('model_architecture.json','r') as f:
        model = model_from_json(f.read())
    model.load_weights('model_weights.h5')

#Compilamos el modelo
model.compile(loss=keras.losses.categorical_crossentropy,
              optimizer='adam',
              metrics=['accuracy'])



#Cambiamos de matriz a vector los valores de las imágenes
x_train = x_train.reshape(60000, 784)
x_test = x_test.reshape(10000, 784)
x_train = x_train.astype('float32')
x_test = x_test.astype('float32')

#Convertir las etiquetas en una matriz binaria
y_test_mat = keras.utils.to_categorical(y_test, num_classes)

#Imprimimos las características de la red neuronal
model.summary()

if APRENDE:

    antes = datetime.now()

    #Entrenamos la red con el conjunto de datos preprocesado
    h = model.fit(x_train,
                  y_train,
                  batch_size = batch_size,
                  epochs = epochs,
                  verbose = 2,
                  validation_data = (x_test, y_test_mat))


    despues = datetime.now()
    t = (despues-antes).seconds
    print("Tiempo de entrenamiento: " + str(t))

    history = h.history

    #Guardamos las características de la red
    with open('history.json', 'w') as f:
        json.dump(history, f)

    model.save_weights('model_weights.h5')
    print("Weights saved in model_weights.h5")

else:
    with open('history.json') as f:
        history = json.load(f)


# Probamos la red con el conjunto de test
y_pred = model.predict_classes(x_test)

# Imprimimos el resultado
errores = np.count_nonzero((y_pred - y_test == 0) == 0)
print("Numero de errores: " + str(errores) + ". Porcentaje de error = " + str(errores/100) + "%.")

if DIBUJA:
    # Mostrar matrix de confusion

    cm = confusion_matrix(y_true, y_pred)
    print(cm)

    # Mostrar aprendizaje
    print(history.keys())
    accuracy =history['acc']
    val_accuracy = history['val_acc']
    loss = history['loss']
    val_loss = history['val_loss']
    epochs = range(len(accuracy))
    plt.plot(epochs, accuracy, 'bo', label='Training accuracy')
    plt.plot(epochs, val_accuracy, 'b', label='Test accuracy')
    plt.title('Training and test accuracy')
    plt.legend()
    plt.show()
    plt.figure()
    plt.plot(epochs, loss, 'bo', label='Training loss')
    plt.plot(epochs, val_loss, 'b', label='Test loss')
    plt.title('Training and validation loss')
    plt.legend()
    plt.show()

    # Mostrar peores errores
    Y_pred = model.predict(x_test)
    Y_pred_classes = np.argmax(Y_pred, axis = 1)
    Y_true = np.argmax(y_test, axis = 1)
    errors = (Y_pred_classes - Y_true != 0)

    Y_pred_classes_errors = Y_pred_classes[errors]
    Y_pred_errors = Y_pred[errors]
    Y_true_errors = Y_true[errors]
    X_val_errors = x_test[errors]

    #Funcion que muestra los 6 errores más comunes
    def display_errors(errors_index,img_errors,pred_errors, obs_errors):
        n = 0
        nrows = 2
        ncols = 3
        fig, ax = plt.subplots(nrows,ncols,sharex=True,sharey=True)
        for row in range(nrows):
            for col in range(ncols):
                error = errors_index[n]
                ax[row,col].imshow((img_errors[error]).reshape((28,28)))
                ax[row,col].set_title("Predicted label :{}\nTrue label :{}".format(pred_errors[error],obs_errors[error]))
                n += 1
        plt.show()
    # Probabilidad de los numeros predichos erroneamente
    Y_pred_errors_prob = np.max(Y_pred_errors,axis = 1)

    # Probabilidades predichas de los valores verdaderos en el conjunto de errores
    true_prob_errors = np.diagonal(np.take(Y_pred_errors, Y_true_errors, axis=1))

    # Diferencia entre la probabilidad de la etiqueta predicha y la etiqueta verdadera
    delta_pred_true_errors = Y_pred_errors_prob - true_prob_errors

    # Lista ordenada de los errores delta
    sorted_dela_errors = np.argsort(delta_pred_true_errors)

    # Top 6 de errores
    most_important_errors = sorted_dela_errors[-6:]

    # Mostrar el top 6 de errores
    display_errors(most_important_errors, X_val_errors, Y_pred_classes_errors, Y_true_errors)
