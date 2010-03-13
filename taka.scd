s.waitForBoot{b = Tala.new};
s.waitForBoot{a = PatternPlayer.new};


w = Window("hi",Rect(0,0,400,400)).front;

w.view.background_(Color.blue(1.))


x = EZSlider.new

x.action = {|ez|ez.value.postln;}

b.tGUI.

b = Button(w, 50.asPoint)
	.states_([
		["M", Color.white, Color.blue(1.5)],
		["M", Color.white, Color.blue(0.8)]
	])
	.action_({|button|
		button.class.dumpInterface;;
	});
	
b.action_({|button|
	
})



\db.asSpec

6.dbamp