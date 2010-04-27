/*
	TODO Parsing groups of xs to make takadimi takita etc. Or just allow 'Takadimi' etc.
	TODO Extend pattern box when end is reached

	TODO Draw graph showing pattern against Tala
	TODO Enter key creates new line, click button to set?	

	TODO Setting playback method iterates over all jatis and changes
	
	FIXME Stopping needs to do midiOffs 	
*/

KonaPlayer {
	var <pattern;
	var <tala;
	var <jatis;
	var <jatiController;
	var <jatisRoutine;
	var <>pView;
	
	*new { 
		^super.new.init;
	}

	init { 
		tala 			= Tala.new(60, 4, false);
		jatis			= List[];
		pattern			= "Xxxx";
		
		jatiController = JatiController.new;
				
		pView 		= KonaPlayerView.new(this);
		tala.tView	= pView.tView;
		
		this.pattern_("Xxxx");
		
		tala.stopFunc = { this.stop };
		
		
	}
		
	createJatisRoutine {
		jatisRoutine = Routine {
			jatis.do {|item, i|
				item.play(tala.clock);
				item.duration.wait;
			};
		};
		jatisRoutine = jatisRoutine.loop;
		
		tala.syncRoutines.clear;
		tala.syncRoutines.add(jatisRoutine);
	}
	
	pattern_ {|newPattern|
		tala.tView.playStopButton.valueAction_(0);
		pattern = newPattern;
		
		this.createJatis;
	}
	
	createJatis {
		var octave = 0;
		
		jatis.clear;
		
		//convert lower case s and p to upper case
		pattern = pattern.collect({ |item, i| if(item==$s || (item==$p)) {item.toUpper} {item} });
		pView.patternField.string_(pattern);
			
		//Split up the string into the various jatis, removing trailing spaces
		// pattern.split($ ).do { |item, i|
		pattern.split($ ).reject({ |item, i| item.size == 0}).do { |item, i| 
			var syllables;
			var jati;
			var gati = tala.gati;
			var karve = 1;
			var split = item.split($:);
			
			if(split.size>1) {
				//Get the multiplier (number of __)
				karve = 1/(2 ** split[0].count({|item, i| item==$_}));
				gati = split[0].findRegexp("[1-9]")[0] ?? tala.gati;
				if(gati!=tala.gati) { gati = gati[1].asInteger };
			};
			syllables = split.last;
			
			jati = Jati(syllables, gati, karve)
				.originalOctave_(octave)
				.octave_(octave);
				
			jatis.add(jati);
			
			[$+,$-].do { |jtem, j|
				item.occurrencesOf(jtem).do { |ktem, k|
					switch (jtem)
						{$+}	{octave = octave + 1}
						{$-}	{octave = octave - 1};		
				};
			};
			
		};	
		
		this.createJatisRoutine;
		
	}
	
	play {
		jatisRoutine.play(tala.clock, 1);
	}
	
	stop {
		jatisRoutine.stop;
		jatis.do { |item, i|
			item.stop;
		};
		this.createJatisRoutine;
	}
	
	isPlaying {
		^tala.isPlaying;
	}
}
