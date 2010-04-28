/*
	TODO GUI for Jatis
	
	-Set sruti for all
	-Select sample set
	-MIDI On/Off
	-Synth On/Off
*/
JatiController {
	var <>jatis;			//	The collection of jatis to control
	var <midiPlayback;		//	Boolean 
	var <synthPlayback;		//	Boolean
	var <amp;				//	Amplitude 
	var <mute;
	var <sruti;
	var <>view;
	var <midiOut;
	var <>midiDeviceString;
	var <>midiNameString;
	
	*new { |aCollection, aGuiBool=true|
		^super.new.init(aCollection, aGuiBool);
	}

	init { |aCollection, aGuiBool|		
		if(aGuiBool) {
			view = JatiControllerView.new;
			view.controller_(this);
		};
		jatis = List[].addAll(aCollection);
		amp = 0.5;
		mute = 1;
		midiPlayback = false;
		synthPlayback = true;
		sruti = 60;

	}
	
	add { |aJati|
		jatis.add(aJati);
		this.updateJatisl
	}
	
	addAll { |aCollection| 
		jatis.addAll(aCollection);
		this.updateJatis;
	}
	
	updateJatis {
		this.setController;
		this.midiPlayback_(midiPlayback);
		this.synthPlayback_(synthPlayback);
		this.amp_(amp);
		this.mute_(mute);
		this.sruti_(sruti);
	}
	
	clear {
		jatis.clear;
	}
	
	midiPlayback_ { |aBoolean| 
		midiPlayback = aBoolean;
		if(midiPlayback && (midiOut==nil)) {
			this.initMIDI;
		};
		
		jatis.do { |item, i| 
			midiPlayback.postln;
			item.midiPlayback_(midiPlayback);
		}
	}
	
	synthPlayback_ { |aBoolean| 
		synthPlayback = aBoolean;
		
		jatis.do { |item, i| 
			item.synthPlayback_(synthPlayback);
		}
	}
	
	amp_ { |aAmp|
		amp = aAmp;
		jatis.do { |item, i| 
			item.amp_(amp*mute);
		};
	}
	
	mute_{|aMute|
		mute = aMute;
		this.amp_(amp);
	}
	
	sruti_ {|aSruti|
		sruti = aSruti;
		jatis.do { |item, i|
			item.sruti_(sruti);
		};
	}
	
	initMIDI {
		var newDeviceArray = List[];
		
		MIDIClient.init;
		if(view!=nil) {			
			MIDIClient.destinations.do { |item, i|	
				newDeviceArray.add([item.device, item.name]);
			};
			view.midiDeviceArray = newDeviceArray;
			view.refreshMidiDevices;
		};	
	}
	
	setMidiOut{|device, name|
		midiDeviceString = device;
		midiNameString = name;
		midiOut = MIDIOut.newByName(midiDeviceString, midiNameString);
		midiOut.latency = 0;
	}
	
	setController {
		jatis.do { |item, i|
			item.controller = this;
		};
	}
	
}