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
 SFG2 manual part
 */

//correct the rectangle position : below of articular cartilage without any cortical bone or osteophyte
//select "run script for image"

//keep bone-rectangle common part
import qupath.lib.roi.RoiTools
import qupath.lib.objects.PathAnnotationObject
def subchondral = getAnnotationObjects().find {it.getPathClass() == getPathClass("Cartilage")}
def bone = getAnnotationObjects().find {it.getPathClass() == getPathClass("Stroma")}
def plane = bone.getROI().getImagePlane()
if (plane != subchondral.getROI().getImagePlane()) {
    println 'Annotations are on different planes!'
    return    
}
def g1 = subchondral.getROI().getGeometry()
def g2 = bone.getROI().getGeometry()
def common = g1.intersection(g2)
def roi = GeometryTools.geometryToROI(common, plane)
def annotation = PathObjects.createAnnotationObject(roi, getPathClass('TrabArea'))
    addObject(annotation)
    selectObjects(annotation)

//remove hole from empty osteoblast cytoplasm or unwanted area as cartilage from "TrabArea" using brush tools
//duplicate annotation TrabArea  : objects -> annotations -> duplicate annotation
//change Class to "Osteophyte" : Osteophyte -> set class (bo matter if it is actually not osteophyte :) ) 
//Add bone narrow area in "Osteophyte" which will become total bone area, do not paid attention to go out the rectangle border, it will be cut after
//Save  - next SFG3 will cut the "Osteophyte" with the same rectangle and create BoneArea
