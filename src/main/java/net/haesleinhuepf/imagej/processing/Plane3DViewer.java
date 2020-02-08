package net.haesleinhuepf.imagej.processing;


import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.PointRoi;
import ij.measure.Calibration;
import ij.plugin.PlugIn;
import processing.core.PImage;
import processing.event.MouseEvent;

import javax.swing.*;


/**
 * Implements an ImageJ/Fiji plugin showing a 3D Viewer based on Processing.org.
 *
 * Most of the code was written BY Jerome Mutterer and originates from here:
 *   https://github.com/mutterer/ImageJ_Processing3D_Viewer/blob/master/ImageJ_Processing3D_Viewer/ImageJ_Processing3D_Viewer.pde
 *
 * @author haesleinhuepf
 */
public class Plane3DViewer extends processing.core.PApplet implements PlugIn {

    private float rotx;
    private float roty;
    private float rotz = 0;
    private float zoom;
    private ImagePlus imp;
    private float z;
    private PImage slice;

    @Override
    public void run(String arg) {
        imp = IJ.getImage();

        runSketch();
    }

    public void settings() {
        size(400, 400, P3D);
    }

    public void setup() {
        //size(400, 400, P3D);
        rotx = 0.85f;
        roty = 0.35f;
        zoom = 1.5f;
        //ij = new ImageJ(ImageJ.EMBEDDED);
        String path = sketchPath()+"/data/mri-stack.tif";
        // imp = IJ.openImage(path);
        imp.show();
        colorMode(RGB, 255);
    }

    public void draw() {
        ambientLight(100, 100, 100);
        directionalLight(255, 255, 255, -1, 1, 0);
        int [] dims = imp.getDimensions();
        Calibration cal = imp.getCalibration();
        z = (int) ((imp.getZ()-dims[3]/2)*cal.pixelDepth);
        slice = createImage(imp.getWidth(), imp.getHeight(), RGB);
        for (int i = 0; i < slice.pixels.length; i++) {
            slice.pixels[i] = color((int) imp.getProcessor().getf(i));
        }
        background(128);
        translate(width/2, height/2);
        rotateX(rotx);
        rotateY(roty);
        scale(zoom);
        PointRoi r = (PointRoi) imp.getRoi();
        if (r!=null) {
            java.awt.Point[] points = r.getContainedPoints();
            for (int  i = 0; i<r.size(); i++) {
                pushMatrix();
                try {
                    translate(points[i].x-slice.width/2, points[i].y-slice.height/2, ((float)((r.getPointPosition(i) -dims[3]/2)*cal.pixelDepth))) ;
                    colorMode(HSB, dims[3], 100, 100);
                    fill(r.getPointPosition(i), 100, 100);
                    noStroke();
                    sphere(5);
                    colorMode(RGB, 255);
                }
                catch (Exception e) {
                }
                popMatrix();
            }
        }
        pushMatrix();
        fill(255, 0, 0);
        sphere(1);
        fill(255);
        ambientLight(255, 255, 255);
        beginShape();
        texture(slice);
        vertex(-slice.width/2, -slice.height/2, z, 0, 0);
        vertex(slice.width/2, -slice.height/2, z, slice.width, 0);
        vertex(slice.width/2, slice.height/2, z, slice.width, slice.height);
        vertex(-slice.width/2, slice.height/2, z, 0, slice.height);
        endShape();
        noFill();
        stroke(255);
        box(slice.width, slice.height, (float) (dims[3]*cal.pixelDepth));
        noStroke();
        popMatrix();
        IJ.showStatus("rx "+IJ.d2s(rotx, 2)+" ry "+IJ.d2s(roty, 2)+" rz "+IJ.d2s(rotz, 2)+" zoom "+IJ.d2s(zoom, 2));
    }

    public void mouseDragged() {
        float rate = 0.01f;
        rotx += (pmouseY-mouseY) * rate;
        roty += (mouseX-pmouseX) * rate;
    }
    public void mouseWheel(MouseEvent event) {
        float e = event.getCount();
        zoom = zoom+0.001f*e;
    }

    // For testing
    public static void main(String... args) {

        new ImageJ();
        IJ.open("https://imagej.nih.gov/ij/images/t1-head.zip");

        new Plane3DViewer().run("");
    }

}

