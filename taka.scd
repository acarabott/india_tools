s.waitForBoot{x = 10.collect{ |item, i| Jati("SrGmPdN", 4, 1)}};
s.boot;
y = JatiController.new
y.addAll(x)

y.sruti = 90
fork {
	inf.do { |i|
		x[0].play;
		1.5.wait;		
	};
}

z = JatiControllerView.new
MIDIOut.newByName("IAC Driver", "Bus 1").
MIDIClient.destinations[0].device


MIDIClient.init;
x = List[];
MIDIClient.destinations.do { |item, i|	
	x.add([item.device, item.name]);
};
x.collect { |item, i| item.reduce('+')}

x[0].reduce('+')
Collection.dumpAllMethods;
Array.dumpAllMethods;

SCPopUpMenu.dumpAllMethods;

y.midiDeviceString
y.midiNameString
y.view.midiPopUp.valueAction_(0)