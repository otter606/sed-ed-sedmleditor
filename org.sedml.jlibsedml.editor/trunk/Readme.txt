This is the README file for the SED-ED SEDML editor, released February 2012.


What's new in 0.9.9

- Models can now be retrieved from standard URLs as well as Miriam URNs.
- XPath generation now uniquely defines elements. Where possible,
  the generated XPath will use a unique combination of attributes. If this is not possible,
   then then indices of elements will be used. This is less preferable,though, as the ordering of 
    elements could change without affecting the meaning of the model.
- Support is added for generating XPath expressions using namespaced attributes. 

What's new in 0.9.7

- Element IDs are now editable.
- HTML note editor now provides HTML wrapper for content.
- You can now view the SED-ML file as an HTML document by selecting
   a SED-ML file and clicking '*View in Browser*'.

What's new in 0.9.5

 - 'Cut' editing functionality added
 - You can now copy and paste between diagrams
 - Bugfix for editing Miriam URNs which caused application to hang.

What's new in 0.9.1

New features
1) Can now obtain SBML models from BioModels referenced by a Miriam URN ( needs internet connection ).
2) Elements that have notes have a note icon displayed inside their shape to indicate this.
3) Adding/editing notes is now un-doable.
4) Navigation around the document 1). 
   For an active editor,if you click on a SEDML  element in the project view, it will be highlighted in the graphical editor.
5) Navigation around the document 2).
	 You can limit the graphical view to only a subset of elements by selecting an 'Output' element and choosing 'REstric View' from 
	  the context menu, this shows only those elements in the workflow needed to produce the output.
See the embedded help documentation for details. 

Any questions or bugs, please email to richard.adamsATed.ac.uk

Richard Adams
Edinburgh, January 2012