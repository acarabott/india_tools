/*
	TODO MIDI output
*/
Jati {
	
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
	var <>midiPlayback;	//	Boolean for MIDI playback
	
	var routine;		//	Routine for playback
	var beenStopped;	//	Boolean, if the stop method has been called (requiring reset before playback);
	
	*new { |jatis, gati, karve|
		^super.new.init(jatis, gati, karve);
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
		
		this.syllables = "Xxxx";
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
		syllables = string;
		jatis = syllables.count({|item, i| "srgmpdnsxo,".includes(item.toLower)});
		duration = jatis * sylDuration;
	}
	
	createRoutine {
		beenStopped = false;

		routine = Routine {
			var play;
			var rest;
			var note;
			var perc = false;
			var octave = 0;
			syllables.do { |item, i|
				//	Set the buffer
				if([$o, $O, $,,].includes(item)) {
					rest = true;
					perc = true;
				} {
					rest = false;
				};
				if([$x, $X].includes(item)) {
					perc = true;
				} {
					perc = false;
				};
				if([$+,$-,$0].includes(item)) {
					play = false;
				} {
					play = true;
				};
				switch (item)
					{$x}	{ bufferIndex=1 }
					{$X }	{ bufferIndex=0 }
					{$S}	{ note = 0 }
					{$r}	{ note = 1 }
					{$R}	{ note = 2 }
					{$g}	{ note = 3 }
					{$G}	{ note = 4 }
					{$m}	{ note = 5 }
					{$M}	{ note = 6 }
					{$P}	{ note = 7 }
					{$d}	{ note = 8 }
					{$D}	{ note = 9 }
					{$n}	{ note = 10 }
					{$N}	{ note = 11 }
					{$0}	{  octave = 0 }
					{$+}	{
								if(syllables[i-1]!=$+) {
									octave = 0;
								};
								octave = octave +1;
							}		
					{$-}	{ 
								if(syllables[i-1]!=$-) {
									octave = 0;							
								};
								octave = octave - 1;
							};						
				if(play) {
					if(perc) {
						if(rest.not) {
							Synth(\simplePlay, [\bufnum, bufferIndex]);
						};
					} {
						note = octave*12 + note + 60;
						Synth(\beep, [\freq, note.midicps]);
					};
					sylDuration.wait;				
				};
			};
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
}