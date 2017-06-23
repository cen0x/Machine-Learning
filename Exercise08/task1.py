"""
Neuronal Network to Classify MNIST data.
"""

from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import argparse
import sys

from tensorflow.examples.tutorials.mnist import input_data

import tensorflow as tf

FLAGS = None

def main(_):
    # Import data
    mnist = input_data.read_data_sets(FLAGS.data_dir, one_hot=True)

    num = 4

    # Create the model
    x = tf.placeholder(tf.float32,[None, 784])

    W1 = tf.Variable(tf.random_uniform([784, num],minval=-0.2,maxval=0.2,dtype=tf.float32))
    b1 = tf.Variable(tf.zeros([num],dtype=tf.float32))#tf.random_uniform([num],minval=-1,maxval=1,dtype=tf.float32))
    W2 = tf.Variable(tf.random_normal([num,  10],stddev=0.5))#random_uniform([num,  10],minval=-0.1,maxval=0.1,dtype=tf.float32))
    b2 = tf.Variable(tf.zeros([10],dtype=tf.float32))#tf.random_uniform([ 10],minval=-1,maxval=1,dtype=tf.float32))
    y = tf.matmul(tf.nn.sigmoid(tf.matmul(x, W1) + b1), W2) + b2

    # Define loss and optimizer
    t = tf.placeholder(tf.float32, [None, 10])
    cross_entropy = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(labels=t, logits=y))
    train_step = tf.train.GradientDescentOptimizer(0.5).minimize(cross_entropy)

    sess = tf.InteractiveSession()
    tf.global_variables_initializer().run()

    # Train
    for _ in range(100000):
        batch_xs, batch_ys = mnist.train.next_batch(100)
        sess.run(train_step, feed_dict={x: batch_xs, t: batch_ys})

    # Test trained model
    correct_prediction = tf.equal(tf.argmax(y, 1), tf.argmax(t, 1))
    accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
    print(sess.run(accuracy, feed_dict={x: mnist.test.images,t: mnist.test.labels}))

    current_min = tf.reduce_min(W1)
    current_max = tf.reduce_max(W1)
    target_min = 0
    target_max = 255
    Wz = tf.reshape(tf.cast((W1 - current_min)*(target_max - target_min)/(current_max - current_min) + target_min,tf.uint8),[28,28,1,num])
    for i in range(num):
        im = tf.image.encode_png(Wz[:,:,:,i]).eval()
        f=open("images/ov"+str(num)+"."+str(i)+".png","wb")
        f.write(im)
        f.close()         
       

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--data_dir', type=str, default='/tmp/tensorflow/mnist/input_data', help='Directory for storing input data')
    FLAGS, unparsed = parser.parse_known_args()
    tf.app.run(main=main, argv=[sys.argv[0]] + unparsed )
