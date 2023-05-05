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
 SFG3 to do after SFG2_manual on all image, save and close
 **************************************************************************
 */
//cut the total bone area with the same rectangle than TrabArea and create BoneArea
def subchondral = getAnnotationObjects().find {it.getPathClass() == getPathClass("Cartilage")}
def bone2 = getAnnotationObjects().find {it.getPathClass() == getPathClass("Osteophyte")}
def plane = bone2.getROI().getImagePlane()
if (plane != subchondral.getROI().getImagePlane()) {
    println 'Annotations are on different planes!'
    return    
}
def g1 = subchondral.getROI().getGeometry()
def g3 = bone2.getROI().getGeometry()
def common = g1.intersection(g3)
def roi = GeometryTools.geometryToROI(common, plane)
def annotation = PathObjects.createAnnotationObject(roi, getPathClass('BoneArea'))
    addObject(annotation)
    selectObjects(annotation)
//delete annotations Other et Stroma
def removal = getAnnotationObjects().findAll{it.getPathClass() == getPathClass("Osteophyte")}
removeObjects(removal, true)    
def removal2 = getAnnotationObjects().findAll{it.getPathClass() == getPathClass("Stroma")}
removeObjects(removal2, true) 

//set % between Trab and total Bone area
def tissue = getAnnotationObjects().find {it.getPathClass() == getPathClass("TrabArea") && it.getROI() instanceof qupath.lib.roi.GeometryROI}
def tissue2 = getAnnotationObjects().find {it.getPathClass() == getPathClass("BoneArea") && it.getROI() instanceof qupath.lib.roi.GeometryROI}
def d1 = tissue2.getROI().getArea()
def d2 = tissue.getROI().getArea()
def areaPercentage2 = (d2 / d1) * 100

//add value in a new col
tissue.getMeasurementList().addMeasurement("Bone volume percentage", areaPercentage2)

//Delete unused annotations before saving  
def removal2 = getAnnotationObjects().findAll{it.getPathClass() == getPathClass("BoneArea")}
removeObjects(removal2, true) 
def removal3 = getAnnotationObjects().findAll{it.getPathClass() == getPathClass("Cartilage")}
removeObjects(removal3, true) 

//Save all before running save.groovy!!!!
    