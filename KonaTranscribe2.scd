w = Window("KonaTranscribe", Rect(400,400,600,400)).front;
SCStaticText
x = TextView(w, Rect(5,5,107,25))
	.string_("Takadimi")
	.background_(Color.grey(0.6))
	.focusColor_(Color.grey(0.8))
	.editable_(false) 
	.font_(Font(Font.defaultMonoFace, 20))

y = TextView(w, Rect(5,35,143,25))
	.string_("Dadiginadom")
	.background_(Color.grey(0.6))
	.focusColor_(Color.grey(0.8))
	.editable_(false) 
	.font_(Font(Font.defaultMonoFace, 20))

z = TextView(w, Rect(115,5,143,25))
	.string_("Dadiginadom")
	.background_(Color.grey(0.6))
	.focusColor_(Color.grey(0.8))
	.editable_(false) 
	.font_(Font(Font.defaultMonoFace, 20))

GUI

GUI.stringBounds