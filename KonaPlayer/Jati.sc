/*
	TODO Input "SrGm or Xxx" instead of number of jatis
	TODO Provide interface for selecting MIDI output possibilities
	TODO Better clapping sound
	TODO Better synth sound
	TODO Balance sounds
	TODO Volume control
	
	FIXME Tisrsa MIDI note doesn't play on first S SrGm SrGm SrGm SrGm        SrGm SrGm SrGm PmGrS
*/

Jati {
	classvar <srutiBase;	//	The base sruti note to add/subtract from
	
	var <jatis;			//	The number of syllables
	var <gati;			//	The beat subdivision
	var <karve;			//	The gati multipler
	var <syllables; 	//	The syllables themselves
	
	var <sylDuration;	//	Duration of one syllable
	var <totalDuration;	//	The relative duration of the routine.
	
	var <duration;		//	The duration of playback
	
	var s;				//	Server.default
	var buffers;		//	Buffers for playback of audio
	var bufferIndex;	//	Index of the buffer to use
	var sounds;			//	The sounds to use for playback
	var kanjiraSounds;	//	Default Kanjira sounds
	var <>sruti;		//	Root note 
	var <>originalOctave;//	The octave of the first syllables
	var <>octave;		//	Octave shift
	
	var <>synthPlayback;//	Boolean for synth playback
	var <midiPlayback;	//	Boolean for MIDI playback
	var midiOut;		//	MIDIOut instance
	
	var routine;		//	Routine for playback
	var beenStopped;	//	Boolean, if the stop method has been called (requiring reset before playback);
	
	*initClass {
		srutiBase = 60;	//	C
	}
	
	*new { |jatis, gati, karve|
		var args = [jatis, gati, karve];
		
		if(args.every({ |item, i| item>0 })) {
			^super.new.init(jatis, gati, karve);
		} {			
			args.do { |item, i|
				var varName;
				if(item<1) {
					switch (i)
						{0}	{varName = "jatis"}
						{1}	{varName = "gati"}
						{2}	{varName = "karve"};
				
					(varName + "needs to be at least 1").postln;
				};
			};
			^nil;
		}
	}

	init { |aJatis, aGati, aKarve|
		jatis = aJatis;
		gati = aGati;
		karve = aKarve;
		sylDuration = ((1/gati)*karve);
		duration = jatis * sylDuration;
		
		s = Server.default;
		kanjiraSounds = ["sounds/KJDIM.wav", "sounds/KJBELL.wav"];
		sounds = kanjiraSounds;
		
		sruti = 3;
		octave = 0;
		originalOctave = octave;
		
		synthPlayback = true;
		midiPlayback = false;
		
		this.syllables = (("x" ! jatis)[0]=$X).reduce('++') ?? "X";
		this.loadBuffers;
		this.loadSynthDefs;
		this.createRoutine;
	}
	
	loadSynthDefs {
		SynthDef(\simplePlay, {| out = 0, bufnum = 0, amp = 1|
			Out.ar(out, 
				(PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum), doneAction:2)*amp).dup
			)
		}).load(s)
	}
	
	loadBuffers {
		buffers = Array.newClear(sounds.size);
		sounds.do { |item, i|
			buffers[i] = Buffer.read(s, item);
		};
	}
	
	syllables_ {|string|
		"string: ".post; (string).postln;
		syllables = string;
		"syllables: ".post; (syllables).postln;
		jatis = syllables.count({|item, i| "srgmpdnsxo,".includes(item.toLower)});
		duration = jatis * sylDuration;
		this.createRoutine;
	}
	
	altOctave { |value|
		octave = octave + value;
	}
	
	createRoutine {
		beenStopped = false;

		routine = Routine {
			var play;
			var rest;
			var note;
			var perc = false;
			
			syllables.do { |item, i|
				
				"item: ".post; (item).postln;
				
				//	If the syllable is a percussive hit (which includes rests)
				if([$x, $o, $,].includes(item.toLower)) {
					perc = true;
					if(item == $o || (item == $,)) {
						rest = true;
					};
				} {
					rest = false;
				};
								
				//	If the syllable is an octave indicator
				if([$+,$-,$0].includes(item)) {
					play = false;
				} {
					play = true;
				};
				
				//	Set correct midi note/buffer
				switch (item)
					{$X }	{ bufferIndex=0 }		//	Percussion 1
					{$x}	{ bufferIndex=1 }		//	Percussion 2
					
					{$S}	{ note = 0 }			//	Sa 			- Root
					{$r}	{ note = 1 }			//	Little ri 	- b2
					{$R}	{ note = 2 }			//	Big Ri		- 2
					{$g}	{ note = 3 }			//	Little ga	- b3
					{$G}	{ note = 4 }			//	Big Ga		- 3
					{$m}	{ note = 5 }            //	Little ma	- 4
					{$M}	{ note = 6 }            //	Big Ma		- #4
					{$P}	{ note = 7 }            //	Pa			- 5
					{$d}	{ note = 8 }            //	Little da 	- b6
					{$D}	{ note = 9 }            //	Big Da		- 6
					{$n}	{ note = 10 }           //	Little ni	- b7
					{$N}	{ note = 11 }           //	Big Ni		- 7
					
					// {$0}	{  octave = 0 }					//	Middle Octave
					{$+}	{ 								//	Up an Octave
								// if(syllables[i-1]!=$+) {
								// 	octave = 0;
								// };
								octave = octave +1;
							}		
					{$-}	{ 								//	Down an Octave
								// if(syllables[i-1]!=$-) {
								// 	octave = 0;							
								// };
								octave = octave - 1;
							};						
				//	Playback
				if(play) {
					1.postln;
					if(perc) {
						2.postln;
						"rest: ".post; (rest).postln;
						"rest.not: ".post; (rest.not).postln;
						if(rest.not) {
							3.postln;
							if(synthPlayback) {
								Synth(\simplePlay, [\bufnum, bufferIndex]);
							};
							if(midiPlayback) {
								4.postln;
								switch (bufferIndex)
									{1}	{note = 40}
									{0}	{note = 36};
									
								midiOut.noteOn(0, note, 100);
							};
						};
					} {
						5.postln;
						note = octave*12 + note + srutiBase + sruti;
						if(synthPlayback) {
							6.postln;
							Synth(\beep, [\freq, note.midicps]);
						};
						if(midiPlayback) {
							7.postln;
							midiOut.noteOn(0, note, 100);
						};
					};
					
					//	Waiting
					8.postln;
					sylDuration.wait;				
					if(midiPlayback) {
						9.postln;
						midiOut.noteOff(0, note, 100);
					};
				};
			};
			octave = originalOctave;
			routine.yieldAndReset;
		};
	}
	
	play {|clock, quant|
		if(beenStopped) { routine.reset	};
		routine.play(clock, quant);
	}
	
	stop {
		routine.stop;			
		beenStopped = true;
	}
	
	midiPlayback_ {|boolean|
		midiPlayback = boolean;
		if(midiPlayback) {
			MIDIClient.init;
			midiOut = MIDIOut.newByName("IAC Driver", "Bus 1");
			midiOut.latency = 0;
		};
	}
}