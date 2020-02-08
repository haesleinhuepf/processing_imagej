## Installation

Clone this repository

```
git clone https://github.com/haesleinhuepf/processing_imagej.git
cd processing_imagej
```

Open the `pom.xml` and enter the location of your Fiji installation in this line:

```
<imagej.app.directory>C:/programs/fiji-win64/Fiji.app/</imagej.app.directory>
```

Download processing-3.5.4-windows64.zip from [processing.org](https://processing.org) and unpack it to the folder `c:/Programs/`. 
If you unzip it to a different directory, please adapt the folder in the pom.xml file. For example in this line:

```
<systemPath>C:/Programs/processing-3.5.4-windows64/processing-3.5.4/core/library/jogl-all.jar</systemPath>
```

Build and deploy the project with maven. This will install the plugin to your Fiji installation directory:

```
mvn install
```

Go to the processing installation folder `/processing-3.5.4/core/library` and 
copy the `jogl-all.jar` to `jogl-all.jar.zip`. 
Open this zip file and unpack its sub-directory `/natives/` to your Fiji installation directory: `Fiji.app/natives/`.

# Testing the installation
* run Fiji
* Open Samples > T1 Head
* Plugins > Processing.org 3D Viewer

# Acknowledgements
Thanks to Jerome Mutterer for [his processing.org code running the 3D Viewer](https://github.com/mutterer/ImageJ_Processing3D_Viewer/blob/master/ImageJ_Processing3D_Viewer/ImageJ_Processing3D_Viewer.pde)!
