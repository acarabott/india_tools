s.waitForBoot{a = PatternPlayer.new};
s.waitForBoot{a = Tala.new};
a.set_gati_amp(2,1)
a.gati_amps
a.stop
a.play
a = TalaGUI.new

a.gati_func = {Synth(\beep, [\freq, 440]);}
CVTest.new
a = Tester.new
a.tGUI.win
s.boot
a.pattern_view
w = Window("test", Rect(0,0,800,400)).front;
w.view
s.boot
a.pattern_view.parent
x = Tala.new(60, 4, false);
t = TalaGUI.new(x,w);
x.tGUI = t
p = PatternPlayer.new
p.pattern_view

a.tala.tGUI.win_bounds.extent.x
a = Tala.new
x = ['x','x','X','X'].findAll(['x','X'])
x.removeAllSuchThat({|item, i| item == ' '})


a.pattern
a.pattern.size

a.tala.gati =6
a.stop
pattern = new_pattern;
a.pattern
~pattern = "oxooxoxx";
~pat_sym = ~pattern.collectAs({|item, i| item.asSymbol }, Array).reject({|item, i| ['x','o'].includes(item.asSymbol).not });
~all_x = ~pat_sym.indicesOfEqual('x');
~all_x.indexOf(3);

r = Routine {
	inf.do {|i,j|
		~func.(i,j);
		0.25.wait;
	};
};
r.play;
r.stop;


~func = {|i,j|
	var index = (j%~pattern.size).asInteger;
	var cur, prev, next;
	if(~pat_sym[index] == 'x') {		
		if(index == ~all_x[0]) {	//	If this is the first x
			Synth(\beep, [\freq, 880]);
/*			"Ta ".post;				//	Make it a Ta!*/
		} {
			cur = ~all_x.indexOf(index);
			prev = ~all_x[cur-1];
			next = ~all_x[cur+1];
			
			if(next != nil && (prev != nil)) {
				
				if((next - index) < (index - prev)) {
/*					"Ta ".post;*/
					Synth(\beep, [\freq, 880]);
				} {
/*					"Dim ".post;*/
					Synth(\beep, [\freq, 440]);
				};
			} {
				Synth(\beep, [\freq, 440]);
/*				"Dim ".post;*/
			};
		};
	};
	
};

Synth(\beep, [\freq, 440]);
~cur = ~all_x.indexOf(3);
~next = ~all_x[~cur+1]
~prev = ~all_x[~cur-1]

~next - 3
3 - ~prev

~all_x
(6%~pattern.size)
~all_x.indexOf((6%~pattern.size))

~index = j%pattern.size;
		var sound;
		var cur, next, prev;
		
		if(pat_sym[index] == 'x') {
			if(index == all_x[0]) { 	//	If this is the first X
				sound = 0;				//		Make it a Ta!
			} {
				cur = all_x.indexOf(index);
				next = all_x[cur+1]; 
				prev = all_x[cur-1];

				if(next != nil && (prev != nil)) {
					if((next - cur) < (cur - prev)) {
						"Ta".post;
					};
				} {
					"dim".postln;
				};
			};
				
				if(pat_sym[index-1] == 'o' ) {
					sound = 0;
				} {
					sound = 1;						
				};
			};
		};
		
		if(sound!=nil) {
			Synth(\simple_play, [\bufnum, buffers[sound]]);
		};			
	}
}

w = Window("hi", Rect(200,200,400,200)).front;
a = KonaWordView(w, Rect(10,10,200,300)).numLines_(10);
b = KonaWordView(w, Rect(110,10,200,300));
init { |aParent, aBounds, aJatis=4, aNumUnderlines=0|




TalaGUI: changed win_bounds to bounds

w = Window("hi", Rect(40,40,400,400)).front
a = CompositeView(w, Rect(0,0,100,100)).background_(Color.red);

x = Window("hi", Rect(40,40,400,400)).front
a.parent = x
SCCompositeView


true.not

y = 5
x = y ?? 4


a = b = 4

b