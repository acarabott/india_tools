x = "s,,,s,,rs,,r";

t = TempoClock.new(1);
r = Routine {
	var freq;
	inf.do {|i|
		var play = false;
		switch (x.wrapAt(i).asSymbol)
			{'s'}	{freq = 60; play = true}
			{'r'}	{freq = 61; play = true}
			{'R'}	{freq = 62; play = true}
			{'g'}	{freq = 63; play = true}
			{'G'}	{freq = 64; play = true}
			{'m'}	{freq = 65; play = true}
			{'M'}	{freq = 66; play = true}
			{'P'}	{freq = 67; play = true}
			{'d'}	{freq = 68; play = true}
			{'D'}	{freq = 69; play = true}
			{'n'}	{freq = 70; play = true}
			{'N'}	{freq = 71; play = true}
			{'S'}	{freq = 72; play = true};
		
		"freq: ".post; (freq).postln;
		if(play) {
			Synth(\beep, [\freq, freq.midicps, \amp, 2]);
		};
		0.25.wait;
	};
};
r.play(t)
r.stop
r.reset

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