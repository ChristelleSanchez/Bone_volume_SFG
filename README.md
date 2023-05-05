# Bone_volume_SFG
Script for semi-automated bone volume quantification in epiphyse area on Safranin-O/Fast green/Hematoxylin Slides  

Designed for mouse knee coronal section, whith image of medial OR lateral plateau (only one plateau in the image)

See png provided to illustrate the area obtained

Contain two automated parts (SFG1 and SFG3) separated by short manual processing SFG2_manual on each image


<b>Data collected :</b>
 - Bone volume in percentage of bone in a bone area (BV/TV)
 
 <b>NOTE : </b>
 - you can adjust the pixel size to your image (SFG1.groovy), but it will not change the result because you will obtain a value in percentage
 - check the RGB vector, in particular background, and adapt if needed to you images
 - you an easily adapt it to any bone tissue slide stained with fast green or light green, other staining (safranin-o and hematoxylin) are not critical, because the detection is green-based
 
 **************************************************************************
<b> Tutorial</b>
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
When all images are processed, save and close Qupath, then open again and run the script SFG3.groovy Run for project, 
save (critical step) then run save.groovy to collect all data in .tsv file. 
