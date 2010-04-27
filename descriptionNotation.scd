SynthDef(\gamaka) { |freq=440, amp=0.1, lagTime=0.1, trig=0, out=0|
	var ramp = Ramp.kr(freq, lagTime);
	var sig = SinOsc.ar(ramp, 0, amp);
	var env = EnvGen.ar(Env.adsr(0.1,1,0.05,0.1),trig);
	
	Out.ar(out,
		Pan2.ar(sig*env, 0)
	);
}.load(s);

x = Synth(\gamaka, [\freq, 440, \trig, 1]);
x.set(\freq, 880, \trig, 1);
{
	l = 0.5;
	[60,61,64,65,67,68,71,72].do { |item, i|
		x.set(\freq, item.midicps, \lagTime, l/2);
		l.wait;
	};
}.fork;

x.set(\freq, 60.midicps);


