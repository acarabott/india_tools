s.boot

a = KonaTani.new(70, #["I4", "O", "O"], 4, argSynth:\MIDIPlay);

b = KonaWord.new(4,4,1,a);
c = KonaWord.new(5,4,1,a);
3.do { |i|
	7.do { |i|
		a.add(b)
	};
	a.add(c);	
};
6.do { |i|
	a.add(b);
};
a.add(c);

a.clear

a.play
a.stop


a.tala_vol = 7
a.stop
KonaGenerator.dumpInterface;
a.class.dumpInterface;
a.play
a.stop
a.class.dumpInterface;

50*16
b = a.gen.randomMora(16*4, 4, 1)

a.add(b)
a.play
a.stop
a.clear
b[2].postWord
b.postWord
b.do { |item, i|
	item.postWord
};

b = KonaWord.new(8,4,1,a)
c = KonaTime.new
c.add(b)
c.play
b.play
a.rout
b.word.size.do { |i|
	i.postln;
};

{
	inf.do { |i|
		x = a.gen.randomDensityJati(c,1);
		x.play		
	};
}.fork
x.postWord
x.play
a.fftBuff
tani

a = Tala.new

a.khanda
a.play
a.stop

s.boot
a = PatternPlayer.new
a.play
a.stop

a.tempo = 100
a.stop
a.pattern = "xo"
a.stop

~clock = TempoClock.new(1);

x = Routine {
	inf.do { |i|
		Synth(\simple_play, [\bufnum, a.buffers[0]]);
		1.wait;
		3.do { |i|
			Synth(\simple_play, [\bufnum, a.buffers[1]]);
			1.wait;			
		};
		2.do { |i|
			Synth(\simple_play, [\bufnum, a.buffers[0]]);
			1.wait;
			Synth(\simple_play, [\bufnum, a.buffers[1]]);
			1.wait;			
		};
	};
};

y = Routine {
	inf.do { |i|
		Synth(\simple_play, [\bufnum, a.buffers[1]]);
		0.5.wait;
	};
};

x.reset
x.stop;y.stop;
x.reset;y.reset;
x.play(~clock, 1);
y.play(~clock, [8,0]);

Stream
{ 
	y.stop;
	"stop!".postln;
	1.5.wait;
	"ok".postln;
	y.reset;
				
}.fork
y.play(~clock, [4,1]);
y.stop
Quant
TempoClock


a = Tala.new
a.laya = 120
a.drone_note = a.drone_note -1
a.drone_amp = 3
a.stop
a.drone

s.boot;
a = PatternPlayer.new
a.playa
a.stop
a.tempo = 120
a.pattern = "xooo"
a.stop
a.play
a.stop
a.no_play
a.play
a.stop
a.stop