# Image Segmentation
This application converts an RGB image to HSV color space. The 
corresponding HSV values are then segmented based on a hue threshold [h1 - h2]. All
colors outside this range are converted to grayscale, while colors within this range displayed as normal.

## Execution
To invoke the run the command  java src/ImageSegmentation C:/myDir/myImage.rgb h1 h2 were h2 > h1.
