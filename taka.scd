s.boot;
x = Jati("SrGmSrGm",3,1);
x.play;

a.jatis.do { |item, i|
	item.midiPlayback_(true);
	item.synthPlayback_(false);
};

x.karve = 0.5

s.waitForBoot{a = KonaPlayer.new};
x = Jati("+SrGmSrGmSrGmSrGmSrGmSrGmSrGmPmGrS",3,1)
	.midiPlayback_(true)
	.synthPlayback_(false);

x.play

Synth(\beep, [\freq, 60.midicps]);

SynthDef(\beep) { |out=0, freq=440, amp=0.1, pan=0|
	var saw = LFSaw.ar(freq, 0, amp/3);
	var sin = SinOsc.ar(freq, 0, amp);
	var env = EnvGen.ar(Env.new([0,1,0,],[0.01,0.2]), doneAction:2);
	var sig = Mix.new([saw, sin]);
	
	Out.ar(out,
		Pan2.ar(sig*env, pan);
	);
}.load(s);



"S".count({ |itemo i| item == $S})

Jati(1,4,1)


("xx" ! 1)[0]=$X).reduce('++');

(("x" ! 1)[0]=$X).reduce('++')
x = "x".reduce('++') ?? "X"

SrGmPdN+S

Synth(\beep, [\freq, 440, \amp, 0.2, \pan, 0, \out, 0]);
s.boot;

SoPm Pooo dooo +Sooo Sooo rooo rooo Sooo-dooo Pooo mooo Pooo rooo mooo Pooo dooo mooo Pooo dooo Pooo mooo Gooo rooo Sooo

SoPm Pooo dooo+SoooSooorooo rooo Sooo-dooo Pooo mooo Pooo rooo mooo Pooo dooo mooo Pooo dooo Pooo mooo Gooo rooo Sooo

f = {|note, time, cut|
	fork {
		x.set(\amp, 0.2, \freq, note.midicps, \lagTime, time);
		(time*0.9).wait;
		if(cut) {
			x.set(\amp, 0);
		};
		(time*0.1).wait;		
	}
}

Scale.major.degrees
fork {
	10.do { |i|
		f.(60 + Scale.major.degrees.choose, 0.5, [false, true].choose);
		0.5.wait;	
	};	
}

{
	f.(67, 0.5);
	f.(72, 2);
	f.(73, 0.25)
	f.(72, 0.25)
	
}.fork

