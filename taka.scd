s.boot;
a = PatternPlayer.new
a.play_stop_button.valueAction_(0)
a.play_stop_button.states
SCButton
a.stop

a.tempo
a.tala.tempo
a.tala.play;a.routine.play;
a.stop
a.play

a.tala.play;
a.tala.stop;
a.tala.no_play

x = false;
y = true;

(x==true) || (y==true)

SCStaticText

a.tempo_text.getPropertyList
a.tempo_field
a.

x = SCStaticText
x.getPropertyList
SCNumberBox
a.pattern_field
a.tempo_field.typingColor

SCTextField
bench

~sounds 	= ["sounds/KJDIM.wav", "sounds/KJBELL.wav"];
~buffers = Array.newClear(~sounds.size);
~sounds.do { |item, i|
	~buffers[i] = Buffer.read(s, item);
};

x = {
	var index;
	var item_c;
	var pattern = "xxxx";
	var gati = 4;
	var tempo = 60;
	var wait_time = 60/tempo;
	
	~routine = Routine {
		pattern.do { |item, i|
			item_c = item.toLower.asSymbol;
			if(~sounds.size==2) {
				if(i%pattern.size!=0) {
					if(['x','o'].includes(pattern[i-1].toLower.asSymbol).not) {
						index = 0;
					} {
						index = 1;
					};
				} {
					index = 0;
					
				};
			};
			
			if((item_c=='x') || (item_c=='o')) {
				if(item_c=='x') {
					Synth(\simple_play, [\bufnum, ~buffers[index]]);
				};
				(wait_time/gati).wait;
			};
			

		};
	};
	
	~routine = ~routine.loop;
}

{
10000.do { 
	x.();
}
}.bench

{
	y = x.();
10000.do { 
	z = y.shallowCopy;
};
}.bench



deepCopy




a = Routine {
	10.do { |i|
		i.postln;
		0.1.wait;
	};
};

a.shallowCopy==a
Object


b = a.shallowCopy
a.play
b.play

x = {a};

b = x.()
b.play
a.play
b = a.deepCopy
c = a

a.play
c.play
b.play
a.reset

