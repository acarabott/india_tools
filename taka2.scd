~window.close;
~pattern = "xxxx";
~routine = Routine {
	
	1.wait;
};
w = 400;
h = 100;
~window = Window.new("Pattern Player", Rect((Window.screenBounds.width/2)-(w/2),(Window.screenBounds.height/2)-(h/2),w,h), false)
	.userCanClose_(true)
	.front;
~pattern_field = TextField(~window, Rect(10,10,w-20,20))
	.string_(~pattern)
	.action_({|field| 
/*		this.set_pattern(field.value);*/
/*		this.confirm_set(pattern_set);*/
	});
~pattern_set = StaticText(~window, Rect(70,70,70, 20)).background_(Color.white).align_(\center);
~play_stop_button = Button(~window, Rect(10,40,50,50))
	.states_([
		["Play", Color.black, Color.green],
		["Stop", Color.white, Color.red]
	])
	.action_({|button|
		if(~routine.isPlaying) {
/*			this.stop;*/
			"stop!".postln;
		} {
/*			this.play;*/
			"play!".postln;
		};
	});
~routine_set = StaticText(~window, Rect(70,40,70,20)).background_(Color.white).align_(\center);
~sounds = EZPopUpMenu(
	~window, 
	Rect(w-210,40,200,20), 
	"Sound",
	[
		\Kanjira 	->{|a| 1.postln;},
		\Konakkol 	->{|a| 2.postln;},
		\Custom 	->{|a| 3.postln;}
	]
);

	
