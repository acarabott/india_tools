/*
	TODO MIDI output
	TODO Clean up createRoutine method
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
		jatis = syllables.size;
	}
	
	createRoutine {
		beenStopped = false;

		routine = Routine {
			var play;
			var perc = false;
			var note;
			var octave = 0;
			syllables.do { |item, i|
				//	Set the buffer
				switch (item)
					{$x}	{perc = true; bufferIndex=1; play = true}
					{$X }	{perc = true; bufferIndex=0; play = true}
					{$o}	{perc = true; play = true}
					{$O}	{perc = true; play = true}					
					{$S}	{perc = false; note = 0; play = true}
					{$r}	{perc = false; note = 1; play = true}
					{$R}	{perc = false; note = 2; play = true}
					{$g}	{perc = false; note = 3; play = true}
					{$G}	{perc = false; note = 4; play = true}
					{$m}	{perc = false; note = 5; play = true}
					{$M}	{perc = false; note = 6; play = true}
					{$P}	{perc = false; note = 7; play = true}
					{$d}	{perc = false; note = 8; play = true}
					{$D}	{perc = false; note = 9; play = true}
					{$n}	{perc = false; note = 10; play = true}
					{$N}	{perc = false; note = 11 ; play = true}
					{$0}	{
								octave = 0; 
								play = false;
							}
					{$+}	{
								if(syllables[i-1]!=$+) {
									octave = 0;
								};
								octave = octave +1;
								play = false;						
							}		
					{$-}	{ 
								if(syllables[i-1]!=$-) {
									octave = 0;							
								};
								octave = octave - 1;
								play = false
							};						
				if(play) {
					if(perc) {
						if(item!=$o) {
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


// /* ========================================================================= */
// /* = KonaWord Class - Represents a a single Konakkol word made up of jatis = */ 
// /* ========================================================================= */
// 
// KonaWord {
// 	classvar words;	//Lookup table for syllables to use
// 
// 	var <tani;		//The Tani that this word belongs to
// 	var <word;		//The word the instance represents, an Array of symbols
// 	var <jatis;		//The number of syllables in the word
// 	var <gati;  	//The subdivision of the beat.
// 	var <karve; 	//The number of matras each jati should occupy.
// 	var <matras;	//The number of pulses/sub-divisions in the word;
// 	var <speed;		//The duration wait between syllables
// 	var <dur;		//The duration of the word (the jatis * karve)
// 	var <val;		//Jatis, dur and word in an array for comparison
// 	var <rout;		//The routine for playing this phrase
// 	var konaSynth;	//Symbol of the SynthDef to use
// 	var <accent;	//Additional accent on the first syllable
// 
// 	*initClass {
// 		//Set up lookup table for syllables
// 		words = Array.newClear(10);
// 		words[0] = ['-'];
// 		words[1] = ['Ta'];
// 		words[2] = ['Ta', 'Ka'];
// 		words[3] = ['Ta', 'Ki', 'Tah'];
// 		words[4] = ['Ta', 'Ka', 'Di', 'Mi'];
// /*		words[4] = ['ta', 'ka', 'di', 'mi'];*/
// 		words[5] = ['ta', 'ka', 'ta', 'ki', 'tah'];
// 		words[5] = ['Da', 'Di', 'Gi', 'Na', 'Dom'];
// 		words[6] = ['Ta', 'Ki', 'Tah', 'Ta', 'Ki', 'Tah'];
// 		words[7] = ['Ta', 'Ka', 'Di', 'Mi', 'Ta', 'Ki', 'Tah'];
// 		words[8] = ['Ta', 'Ka', 'Di', 'Mi', 'Ta', 'Ka', 'Ju', 'Na'];
// 		words[9] = ['Da', 'Di', 'Gi', 'Na', 'Dom', 'Ta', 'Ka', 'Di', 'Mi'];
// 
// 	}
// 
// 	*new {|argSyls, argGati, argKarve=1, argTani, argSynth|
// 
// 		//Check arguments aren't nil
// 		if( (argSyls==nil) || (argGati == nil),
// 			{^"arguments not set\n Provide (numSyllables, gati)"}
// 		);
// 
// 		//Check specified group is within bounds, the gati is legit
// 		if( argSyls<=9 && ([4,3,5,7,9].includes(argGati)),
// 			{^super.new.konaWordInit(argSyls, argGati, argKarve, argTani, argSynth) },
// 			{^"Bad Size or Gati"}
// 		);
// 	}
// 
// 	konaWordInit { |argSyls, argGati, argKarve, argTani, argSynth|
// 		tani = argTani;
// 		word = words[argSyls];
// 		jatis = word.size;
// 		gati = argGati;
// 		karve = argKarve;
// 		matras = jatis*karve;
// 		speed = ((1/gati)*karve);
// 		dur = speed * jatis;
// 		val = [jatis, dur, word];
// 		accent = 0;
// 
// 		if(tani!=nil) {
// 			konaSynth = tani.konaSynth
// 		} {
// 			if(argSynth!=nil) {
// 				konaSynth = argSynth
// 			} {
// 				konaSynth = \beep
// 			};
// 		};
// 
// 		this.setRoutine;
// 	}
// 
// 	//Method to set the routine for this word. Stored in a function for re-use
// 	setRoutine {
// 		var ind;
// 		var rate;
// 		var amp;
// 
// 		//MIDI variables
// 		var bOne;				//MIDI note for first beat (always an open sound)
// 		var bOthers;			//Chosen MIDI notes for other beats;
// 		var othersComplete;		//Possible MIDI notes for other beats
// 		var othersTemp;			//Storage for next 'other beat' MIDI note.
// 		var note;				//Chosen MIDI pitch.
// 		var val;				//Temporary storage of chosen MIDI note.
// 		var vel;				//Chosen velocity for MIDI note.
// 
// 		switch (konaSynth)
// 			{nil} {
// 				rout = Routine {
// 					word.size.do {|i|
// 						amp = 0.2;
// 						case
// 						{word[i]=='-'}	{amp=0}
// 						{i==0}	{amp=0.4};
// 
// 						tani.s.bind{Synth(konaSynth, [\amp, (amp+(accent/10)).min(1)])};
// 						speed.wait;
// 					};
// 					yieldAndReset(nil);
// 				};
// 			}
// 
// 			{\konaHit}	{
// 				rout = Routine {
// 					word.size.do { |i|
// 						//Index of the syllable to be played
// 						ind = tani.syls.indexOf(word[i]);
// 						if(i==0) {(amp=0.8+(accent/10)).min(1)} {amp=0.6};
// 						if(word[i]!='-') {
// 							tani.s.bind {
// 								Synth(\konaHit, [\out, 0, \bufnum, tani.fftBuff, \recBuf, tani.buffers[ind], \rate, ((tani.laya/60)*(0.25/speed)).max(1);]);
// 							};
// 							tani.fftRout.next;
// 							if(i==0) {
// 								word[i].post;
// 							} {
// 								word[i].asString.toLower.post;
// 							};
// 							" ".post;
// 							speed.post; " ".post;
// 							speed.wait;
// 
// 						} {
// 							word[i].post; " ".post;
// 							speed.post; " ".post;
// 							speed.wait
// 						};
// 					};
// 					yieldAndReset(nil);
// 				};
// 
// 			}
// 			{\MIDITranscribe} {
// 				rout = Routine {
// 					word.size.do { |i|
// 						if(word[i]!='-') {
// 							if(i==0)
// 								{note = 48; vel = ((70..100).choose+accent).min(127)}
// 								{note = 52; vel = (100+accent).min(127)};
// 							tani.mOut.noteOn(0, note, vel);
// 								if(i==0) {
// 									word[i].post;
// 								} {
// 									word[i].asString.toLower.post;
// 								};
// 							" ".post;
// 							speed.post; " ".post;
// 							speed.wait;
// 							tani.mOut.noteOff(0, note, vel);
// 						} {
// 							word[i].post; " ".post;
// 							speed.post; " ".post;
// 							speed.wait
// 						};
// 					};
// 					yieldAndReset(nil);
// 				};
// 			}
// 
// 			//Automated mapping of strokes for Kanjira virtual instrument
// 			{\MIDIPlay} {
// 					bOne = [36, 37, 38, 39, 45, 46, 47].choose;
// 					othersComplete = (48..55);
// 					othersTemp = Array.newFrom(othersComplete);
// 					bOthers = Array.newClear(word.size-1);
// 					(word.size-1).do { |i|
// 						val = othersTemp.choose;
// 						bOthers[i] = val;
// 						othersTemp = Array.newFrom(othersComplete);
// 						othersTemp.remove(val);
// 					};
// 				rout = Routine {
// 					word.size.do { |i|
// 						if(word[i]!='-') {
// 							if(i==0) {
// 								note = bOne;
// 								vel = (100+accent).min(127);
// 								word[i].post; " ".post;
// 								
// 							} {
// 								note = bOthers[i-1];
// 								vel = (70..100).choose;
// 								word[i].asString.toLower.post; " ".post;
// 								
// 							};
// 							tani.mOut.noteOn(0, note, vel);
// 							speed.post; " ".post;
// 							speed.wait;
// 							tani.mOut.noteOff(0, note, vel);
// 						} {
// 							word[i].post; " ".post;
// 							speed.post; " ".post;
// 							speed.wait
// 						};
// 					};
// 					yieldAndReset(nil);
// 				};
// 
// 
// 			};
// 
// 	}
// 
// 	play {
// 		this.rout.play(tani.clock);
// 	}
// 
// 	//Concatonation method to return a new KonaTime with both KonaItems
// 	++ { |aKonaItem|
// 		var newTime = KonaTime.new();
// 		newTime.add(this).addAll(aKonaItem);
// 		^newTime
// 	}
// 
// 	//Printing method for timing information of the word.
// 	postWord {|argW=true, argD=true, argF=true|
// 		var words, decimals, fractions;
// 		var maxItemLength;
// 
// 		words = Array.newClear(jatis);
// 		decimals = Array.newClear(jatis);
// 		fractions = Array.newClear(jatis);
// 
// 		jatis.do { |i|
// 			words[i] = word[i].asString;
// 			decimals[i] = speed.asString[0..4];
// 			fractions[i] = (speed/4).asFraction(100,false).asString;
// 		};
// 
// 		maxItemLength = [words.maxItem, decimals.maxItem, fractions.maxItem].maxItem.size;
// 
// 		jatis.do { |i|
// 			var numSpaces;
// 			numSpaces = maxItemLength - words[i].size;
// 			numSpaces.do {
// 				words[i] = words[i] ++ " ";
// 			};
// 			numSpaces = maxItemLength - decimals[i].size;
// 			numSpaces.do {
// 				decimals[i] = decimals[i] ++ " ";
// 			};
// 			numSpaces = maxItemLength - fractions[i].size;
// 			numSpaces.do {
// 				fractions[i] = fractions[i] ++ " ";
// 			};
// 		};
// 
// 		if(argW) {
// 		};
// 		if(argD) {
// 		};
// 		if(argF) {
// 		};
// 
// 		^[words, decimals, fractions];
// 
// 	}
// 
// 	//For adding additional accents to the first syllable
// 	accentFirst {
// 		accent = accent + 10;
// 	}
// 
// }
