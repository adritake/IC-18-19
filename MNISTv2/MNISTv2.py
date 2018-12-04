APRENDE = False
DIBUJA = True

batch_size = 128
num_classes = 10
epochs = 20

import numpy as np #algebra lineal
import pandas as pd #Procesamiento de datos
import matplotlib.pyplot as plt #para dibujar
from collections import Counter
from sklearn.metrics import confusion_matrix
import itertools
import seaborn as sns
from subprocess import check_output
import json

import keras
from keras.datasets import mnist #base de datos de los digitos
from keras.models import Sequential
from keras.models import model_from_json
from keras.layers import Dense, Dropout, Flatten, Conv2D, MaxPool2D, Activation
from keras.layers.normalization import BatchNormalization
from keras.preprocessing.image import ImageDataGenerator
from keras.callbacks import ReduceLROnPlateau
from sklearn.model_selection import train_test_split
from sklearn.metrics import confusion_matrix


print("Reconocimiento de dígitos MNIST: APRENDE = " + str(APRENDE) +
      ", DIBUJA = " + str(DIBUJA))

#Cargamos los datos
(x_train, y_train), (x_test, y_test) = mnist.load_data()


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


if APRENDE:
    model = Sequential()
    model.add(Dense(784, input_shape=(784,)))
    model.add(Activation('tanh'))
    model.add(Dropout(0.2))

    model.add(Dense(10))
    model.add(Activation('softmax'))
    with open('model_architecture.json','w') as f:
        f.write(model.to_json())
    print("Model saved in model_architecture.json")

else:
    with open('model_architecture.json','r') as f:
        model = model_from_json(f.read())
    model.load_weights('model_weights.h5')


model.compile(loss=keras.losses.categorical_crossentropy,
              optimizer='adam',
              metrics=['accuracy'])

#Creamos una reduccion de la tasa de aprendizaje
learning_rate_reduction = ReduceLROnPlateau(monitor='val_acc',
                                            patience=3,
                                            verbose=1,
                                            factor=0.5,
                                            min_lr=0.0001)

"""
#Creamos variaciones aleatorias en las imagenes
datagen = ImageDataGenerator(
        featurewise_center=False,  # set input mean to 0 over the dataset
        samplewise_center=False,  # set each sample mean to 0
        featurewise_std_normalization=False,  # divide inputs by std of the dataset
        samplewise_std_normalization=False,  # divide each input by its std
        zca_whitening=False,  # apply ZCA whitening
        rotation_range=15, # randomly rotate images in the range (degrees, 0 to 180)
        zoom_range = 0.1, # Randomly zoom image
        width_shift_range=0.1,  # randomly shift images horizontally (fraction of total width)
        height_shift_range=0.1,  # randomly shift images vertically (fraction of total height)
        horizontal_flip=False,  # randomly flip images
        vertical_flip=False)  # randomly flip images

#Aplicamos las variaciones aleatorias a las imágenes
x_train = x_train.reshape(x_train.shape[0],28,28,1)
datagen.fit(x_train)
"""
#Cambiamos de matriz a vector los valores de las imágenes
x_train = x_train.reshape(60000, 784)
x_test = x_test.reshape(10000, 784)
x_train = x_train.astype('float32')
x_test = x_test.astype('float32')

#y_train = keras.utils.to_categorical(y_train, 10)
y_test = keras.utils.to_categorical(y_test, num_classes)

#Imprimimos las características de la red neuronal
model.summary()

if APRENDE:
    #Entrenamos la red con el conjunto de datos preprocesado
    h = model.fit(x_train,
                  y_train,
                  batch_size = batch_size,
                  epochs = epochs,
                  verbose = 2,
                  validation_data = (x_test, y_test))

    history = h.history

    with open('history.json', 'w') as f:
        json.dump(history, f)

    model.save_weights('model_weights.h5')
    print("Weights saved in model_weights.h5")
else:
    with open('history.json') as f:
        history = json.load(f)


y_pred = model.predict_classes(x_test)


if DIBUJA:
    # Mostrar matrix de confusion
    y_true = np.argmax(y_test,axis=1)
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
    # Errors are difference between predicted labels and true labels
    Y_pred = model.predict(x_test)
    Y_pred_classes = np.argmax(Y_pred, axis = 1)
    Y_true = np.argmax(y_test, axis = 1)
    errors = (Y_pred_classes - Y_true != 0)

    Y_pred_classes_errors = Y_pred_classes[errors]
    Y_pred_errors = Y_pred[errors]
    Y_true_errors = Y_true[errors]
    X_val_errors = x_test[errors]

    def display_errors(errors_index,img_errors,pred_errors, obs_errors):
        """ This function shows 6 images with their predicted and real labels"""
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
    # Probabilities of the wrong predicted numbers
    Y_pred_errors_prob = np.max(Y_pred_errors,axis = 1)

    # Predicted probabilities of the true values in the error set
    true_prob_errors = np.diagonal(np.take(Y_pred_errors, Y_true_errors, axis=1))

    # Difference between the probability of the predicted label and the true label
    delta_pred_true_errors = Y_pred_errors_prob - true_prob_errors

    # Sorted list of the delta prob errors
    sorted_dela_errors = np.argsort(delta_pred_true_errors)

    # Top 6 errors
    most_important_errors = sorted_dela_errors[-6:]

    # Show the top 6 errors
    display_errors(most_important_errors, X_val_errors, Y_pred_classes_errors, Y_true_errors)
