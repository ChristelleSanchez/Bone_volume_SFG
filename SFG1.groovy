/*
April 2023, Qupath version  0.3.2
BSD 3-Clause License
@author Christelle Sanchez
 
 contact: Christelle.sanchez@uliege.be
 University of Liege - Belgium
 **************************************************************************
 Script for semi-automated bone volume quantification in epiphyse area on Safranin-O/Fast green/Hematoxylin Slides
 Designed for mouse knee coronal section, whith image of medial OR lateral plateau (only one plateau in the image)
 Contain two automated parts (SFG1 and SFG3) separated by short manual processing SFG2_manual on each image

 Data collected :
 - Bone volume in percentage of bone in a bone area (BV/TV)
 
 **************************************************************************
 Tutorial
1)	Capture images of the stained slides. Parameters here used Zeiss axio lab.A1 microscope with Axiocam 305 color camera and ZEN software, magnification 10x in .TIF or .PNG. Blank balance adjusted on the first slide, then keep the same parameters, with the plateau as horizontal as possible. All articular cartilage (medial or lateral) must be present in the image, as well as the growth plate. 
2)	Create an empty folder somewhere
3)	Open Qupath, File -> Project -> Create project, and select this folder 
4)	Import images in the project : File -> Project -> Add images 
5)	Delete all unusable images (folded, broken, pleated, really too dark or too light)
6)	Save (File -> save) and close Qupath
7)	Go to the folder, delete the « classifiers » folder in it, and replace it by « classifiers » folder provided here. Also add the folder « scripts » containing the scripts 
8)	Open againg Qupath and the project, without opening any images
9)	Open SFG1.groovy script (Automate -> Show script editor -> open)
10)	Apply SFG1 to all images in the project (Script editor -> Run -> Run for project)
11)	Save (File -> save)
12) Do manual part:
Open each images one by one to do the manual part : see SFG2_manual.groovy
-Open the next image and save the project.
When all images are processed, save and close Qupath, then open again and run the script SFG3.groovy Run for project, save then run save.groovy to collect all data in .tsv file. 

 **************************************************************************
 */

setImageType('BRIGHTFIELD_OTHER');
setPixelSizeMicrons(0.720000, 0.720000);
import qupath.lib.objects.PathObjects
import qupath.lib.roi.ROIs
import qupath.lib.regions.ImagePlane
//add a rectangle in ROI, 1100px large
int z = 0
int t = 0
def plane = ImagePlane.getPlane(z, t)
def roi = ROIs.createRectangleROI(730, 550, 1100, 1100, plane)
def sb = PathObjects.createAnnotationObject(roi, getPathClass('Cartilage'))
addObject(sb)

//analyse-preprocessing-estimate stain vector  (if needed)
//click on Yes to aggree with background color
//click on auto in auto detect windows to check adjustment
//hematoxyline = cyan, DAB = orange, residuel = green
//setColorDeconvolutionStains('{"Name" : "SFG-estimated", "Stain 1" : "Hematoxylin", "Values 1" : "0.8864 0.34676 0.3067", "Stain 2" : "DAB", "Values 2" : "0.08009 0.48918 0.8685", "Background" : " 243 207 215"}');
//manual
setColorDeconvolutionStains('{"Name" : "SFG-estimated", "Stain 1" : "FastGreen", "Values 1" : "1 0 0", "Stain 2" : "SafraninO", "Values 2" : "0.09399 0.49297 0.86495", "Stain 3" : "Hematox", "Values 3" : "0.65111 0.70119 0.29049", "Background" : " 243 207 215"}');
//create thresholder, full, FastGreen, Gaussian, 0.1, 0.05, stroma, ignore, everywhere, name fastgreen
//create object, considering hole to 40px - to avoid hole for osteoblast nucleus: (if needed, test the fastgreen2 provided)
createAnnotationsFromPixelClassifier("fastgreen", 200000.0, 120.0)
//stop script - save and do SFG2_manual

