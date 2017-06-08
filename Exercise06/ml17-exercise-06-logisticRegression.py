# Fill the two gaps marked with "HomeworkTodo"

# This script is a heavily modified sketch by Tom Dupre,
# originally taken from here (remove \, linebreaks and comments)
# http://scikit-learn.org/stable/auto_examples/linear_model/\
# plot_logistic_multinomial.html#sphx-glr-auto-examples-line\
# ar-model-plot-logistic-multinomial-py

import numpy as np
import math
import matplotlib.pyplot as plt
from sklearn.datasets import make_blobs
from sklearn.linear_model import LogisticRegression

# <dataGeneration>
# make 2-class dataset consisting of two
# blobs, such that the dataset is not linearly separable.
centers = [[-5, -5], [0, 0], [5, 5]]
xy, clazz = make_blobs(n_samples=300, centers=centers, random_state=0)
clazz = [k % 2 for k in clazz] # this glues 0-th and 2-nd blob into one class
# </dataGeneration>

# <modifiedThis>
def gaussianRbf(centerX, centerY, sigmaSquare, pointX, pointY):
  a = math.sqrt(math.pow((pointX-centerX),2) + math.pow((pointY-centerY),2))
  return math.exp(-math.pow(a,2)/2/sigmaSquare)

def extractFeatures(xy):
  x = xy[0]
  y = xy[1]

  #a = gaussianRbf(4.5,4.8,4,x,y)
  # b = gaussianRbf(-5, -5, 11, x, y)
  #return [a*x, b*y]

  #return [0.28*x*x*x*x-15*x*x, y*y]

  #return [x*y, x*y*y]

  return [x, y] # HomeworkTodo: experiment with more interesting features

# <training>
features = [extractFeatures(ab) for ab in xy]

clf = LogisticRegression(solver='sag', max_iter=10000, random_state=42).fit(
  features, # trains on the extracted features, not directly on coordinates!
  clazz
)
# </training>

# <plotting>
# create a mesh, classify each point in the mesh, color it accordingly
h = .02  # step size of the mesh
x_min, x_max = xy[:, 0].min() - 1, xy[:, 0].max() + 1
y_min, y_max = xy[:, 1].min() - 1, xy[:, 1].max() + 1
xx, yy = np.meshgrid(np.arange(x_min, x_max, h),
                     np.arange(y_min, y_max, h))

Z = clf.predict([extractFeatures(v) for v in np.c_[xx.ravel(), yy.ravel()]])
Z = Z.reshape(xx.shape)
plt.figure()
plt.contourf(xx, yy, Z, cmap=plt.cm.Paired)
plt.title("LogisticRegression with nonlinear features")
plt.axis('tight')

# Plot the training points
colors = "br"
for i, color in zip(clf.classes_, colors):
    idx = np.where(clazz == i)
    plt.scatter(xy[idx, 0], xy[idx, 1], c=color, cmap=plt.cm.Paired)

plt.show()
# </plotting>
