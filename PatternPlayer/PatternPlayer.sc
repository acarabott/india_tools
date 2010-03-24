/*
	TODO Parsing groups of xs to make takadimi takita etc
	TODO Extend pattern box when end is reached
	TODO Draw graph showing pattern against Tala
	TODO Enter key creates new line, click button to set?	
	TODO Setting playback method iterates over all jatis and changes
	
	FIXME Stopping needs to do midiOffs
	FIXME Comma breaks things
	
*/

PatternPlayer {
	var <pattern;
	var <tala;
	var <jatis;
	var <jatisRoutine;
	var <>pGUI;
	
	*new { 
		^super.new.init;
	}

	init { 
		tala 			= Tala.new(60, 4, false);
		jatis			= List[];
		pattern			= "Xxxx";
				
		pGUI 		= PatternPlayerGUI.new(this);
		tala.tGUI	= pGUI.tGUI;
		
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
		tala.tGUI.playStopButton.valueAction_(0);
		pattern = newPattern;
		
		this.createJatis;
	}
	
	createJatis {
		
		jatis.clear;
				
		//Split up the string into the various jatis
		pattern.split($ ).do { |item, i|
			var syllables;
			var jati;
			var gati = tala.gati;
			var karve = 1;
			var split = item.split($:);
			
			if(split.size>1) {
				//Get the multiplier (number of __)
				karve = 1/(2 ** split[0].count({|item, i| item==$_}));
				gati = split[0].findRegexp("[0-9]")[0] ?? tala.gati;
				if(gati!=tala.gati) { gati = gati[1].asInteger };
			};
			syllables = split.last;
			
			jati = Jati(syllables.size, gati, karve ).syllables_(syllables);
			jatis.add(jati);
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
