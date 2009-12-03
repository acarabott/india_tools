Tala {
	
	var <adi;			//	Adi Tala Preset
	var <rupaka;		//	Rupaka Tala Preset
	var <kCapu;			//	Khanda Capu Preset
	var <mCapu;			//	Misra Capu Preset
	
	var <laya;			//	Tempo
	var <wait;			//	Wait time
	var <>kallai;
	var <>gati;
	var <>eduppu;
	
	var <routine;			//	The playback routine
	
	*initClass {
		adi 	= ["I4", "O", "O"];
		rupaka 	= ["U", "O"];
		kCapu 	= ["C", "U", "U"];
		mCapu	= ["U", "C"]
	}
	
	*new {
		^super.new.init;
	}

	init { 
		routine = List[];
		laya 	= 60;
		wait	= 60/laya;
	}
	
	create_routine {
		routine = Routine {
			inf.do { |i|
				"Clap".postln;
				wait.wait;				
			};
		};
	}
	
	laya_ {|new_value|
		laya = new_value;
		wait = 60/laya;
	}	

}


