x = "s"
x = "s,r,G,m,P,d,N,S,"
x = "sr_Gm_PdPd_N_S";
x = "sr_srGm_srGmPdNS_srGm_"
SynthDef(\sine) { |out=0, freq=440, amp=1|
	var sig = SinOsc.ar(freq,0,amp);
	
	Out.ar(out,
		sig.dup	
	);
}.load(s);

r.stop
r.reset
r.play;
r = Routine {
	var midinote;
	var waitTime;
	var defaultWait = 0.25;
	var waitMult = 1;
	var inMult = false;
	var syn = Synth(\sine, [\freq, 440]);
	
	waitTime = defaultWait;
	
	inf.do {|i|
		var play = true;
		var index = (i%x.size).asInteger;
		var subNum = x[index+1..].occurrencesOf($_);
		var subNumOdd = subNum.odd;
		
		switch (x[index])
			{$s}	{midinote = 60; play = true}
			{$r}	{midinote = 61; play = true}
			{$R}	{midinote = 62; play = true}
			{$g}	{midinote = 63; play = true}
			{$G}	{midinote = 64; play = true}
			{$m}	{midinote = 65; play = true}
			{$M}	{midinote = 66; play = true}
			{$P}	{midinote = 67; play = true}
			{$d}	{midinote = 68; play = true}
			{$D}	{midinote = 69; play = true}
			{$n}	{midinote = 70; play = true}
			{$N}	{midinote = 71; play = true}
			{$S}	{midinote = 72; play = true}
			{$,}	{play = true}
			{$_}	{
						if(inMult) {
							if(subNumOdd) {
								"Woah there (1)!\n".postln;
								waitMult = waitMult / 2;
							} {
								if(subNum != 0) {
									"double up (2)!\n".postln;
									waitMult = waitMult * 2;
									inMult = true;
								} {
									"Woah There(3)!\n".postln;
									waitMult = waitMult / 2;
									inMult = false;
								};
							};
						} {
							"Douuuble up!(4)\n".postln;
							waitMult = waitMult * 2;
							inMult = true
						};
				
						play = false;
					};
		// "midinote: ".post; (midinote).postln;
		// "inMult: ".post; (inMult).postln;
		if(play) {
			syn.set(\freq, midinote.midicps);
			(waitTime/waitMult).wait;
		};
	};
};

t = TempoClock(1);
r.play(t);
r.stop
r.reset
t.tempo = 0.5
s.boot;


// Sa	60
// ri	61
// Ri	62
// ga	63
// Ga	64
// ma	65
// Ma	66
// Pa	67
// da	68
// Da	69
// ni	70
// Ni	71
// Sa	72

// Swarasthana Notation
// 
// Single letters
// With dots for octaves
// 	Highest octave 2 dots
// 	Lowest same logic
// 
// Commas for extensions/durations
// 	Semi colon for two comas
// 	
// Speed




x = "__xx__"

~xSym = (x.collectAs({|item,i| item.asSymbol}, Array))

~inMult = false;
x.do { |item, i|
	var subNum = ~xSym[i+1..].occurrencesOf('_');
	var subNumOdd = subNum.odd;
	
	if(item=='_') {
		Post << "i: " << i << " : " << item << "\n";
		"subNumOdd: ".post; (subNumOdd).postln;
		"subNum: ".post; (subNum).postln;
		"~inMult: ".post; (~inMult).postln;
		if(~inMult) {
			if(subNumOdd) {
				"Woah there (1)!\n".postln;
			} {
				if(subNum != 0) {
					"double up (2)!\n".postln;
					~inMult = true;
				} {
					"Woah There(3)!\n".postln;
					~inMult = false;
				};
			};
		} {
			"Douuuble up!(4)\n".postln;
			~inMult = true
		};
	} {
		"".postln;
	};
	
};


