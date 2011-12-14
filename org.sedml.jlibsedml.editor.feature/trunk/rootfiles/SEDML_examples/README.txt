This folder contains example SED-ML files and SBML model files. The examples are 
those used in the SED-ML specification document.
All the examples now use Biomodels URNs to identify the model. In the editor, you can 
still edit the 'source' field of the model to point to the filepath where these model files
 are located, though.

Circadian clock model example:
------------------------------
Clock_BIOMD21.xml - Biomodels 21 Circadian clock model in SBML
ClockSedML.xml - A SED-ML file describing simulations on the above model
ClockSedMLArchive.sedx - A Sedx archive file containing both the above model and SED-ML file

Repressilator model example
---------------------------
 
Elowitz2000_Repressilator.xml  An SBML file of the Elowitz repressilator model.
RepressilatorSEDML.xml - A SED-ML file describing simulations on the above model.
RepressilatorSEDMLArchive.sedx - A Sedx archive file containing both the repressilator model its SED-ML file.

LeloupSBML
----------
In this file, the model refers to a MIRIAM URN, so you don't need the model file on your machine ( but you need
 internet access).