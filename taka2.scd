a = Tala.new
a.drone_synth
a.play
a.drone_amp = 3
a.drone_note = 63
a.laya = 60
a.khanda
a.play
a.stop
a.amp = 4
x = KonaTani.new

y = KonaWord.new(4,4,1,x);

SynthDef(\drone, {|rootNote=59, amp=1|

var signal1, signal2, root, fifth, octaveA, octaveB, env;
root = rootNote.midicps;
fifth = (rootNote+7).midicps;
octaveA = (rootNote+12).midicps;
octaveB = (rootNote-12).midicps;

env = {EnvGen.kr(Env.new(
				 					Array.rand(16, 0, 0.2),  //Random drones
									Array.rand(15, 1, 5),
									'exponential',
									0,
									1))};
signal1 = Mix(SinOsc.ar([root, fifth, [octaveA, octaveB].choose], 0, 0.3*[env, env, env]));
signal2 = Mix(LFSaw.ar([root, fifth, [octaveA, octaveB].choose], 0, 0.4*[env, env, env]));							

Out.ar(	0,
 		Pan2.ar(signal1)*amp,
 		Pan2.ar(signal2, FSinOsc.kr(0.05))*amp
 		);
}).send(s)

x = Synth(\drone, [\rootNote, 63]);
x.set(\amp, 3);
x.free


a.stop

a = KonaTani.new(60)

b = KonaWord.new(1,4,3,a);
c = KonaWord.new(1,4,1,a);
d = KonaWord.new(5,4,1,a);

e = KonaTime.newFrom([b,c],a);
f = KonaTime.newFrom([c,b],a);
g = KonaTime.newFrom([f,e,f,e,f,e,f,d],a);
h = KonaTime.newFrom([f,e,f,e,f,e,d],a);

3.do { |i|
	a.add(e)
};
a.add(d);
a.add()

a.add(g);
a.add(h);


t = Tala.new(60);
t.stop
t.play
t.drone_note = t.drone_note -1
t.drone_amp = 2
t.amp = 5
a.clear
3.do { |i|
	7.do { |i|
		a.add(e);
	};
	a.add(d);	
};
6.do { |i|
	a.add(e);
};
a.add(d);

z = a.talaRout.loop
y = a.rout.loop
a.tala_vol = 10

z.play;
y.play;

z.stop;
y.stop;
a.play
t.laya = 80
t.play
z.reset;
y.reset;

SynthDef