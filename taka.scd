s.waitForBoot{a = KonaPlayer.new};
s.boot;
x = Jati("XoXo",4,1)


"S".count({ |itemo i| item == $S})

Jati(1,4,1)


("xx" ! 1)[0]=$X).reduce('++');

(("x" ! 1)[0]=$X).reduce('++')
x = "x".reduce('++') ?? "X"

SrGmPdN+S

Synth(\beep, [\freq, 440, \amp, 0.2, \pan, 0, \out, 0]);
s.boot;

SoPm Pooo dooo +Sooo Sooo rooo rooo Sooo-dooo Pooo mooo Pooo rooo mooo Pooo dooo mooo Pooo dooo Pooo mooo Gooo rooo Sooo

SoPm Pooo dooo+SoooSooorooo rooo Sooo-dooo Pooo mooo Pooo rooo mooo Pooo dooo mooo Pooo dooo Pooo mooo Gooo rooo Sooo

f = {|note, time, cut|
	fork {
		x.set(\amp, 0.2, \freq, note.midicps, \lagTime, time);
		(time*0.9).wait;
		if(cut) {
			x.set(\amp, 0);
		};
		(time*0.1).wait;		
	}
}

Scale.major.degrees
fork {
	10.do { |i|
		f.(60 + Scale.major.degrees.choose, 0.5, [false, true].choose);
		0.5.wait;	
	};	
}

{
	f.(67, 0.5);
	f.(72, 2);
	f.(73, 0.25)
	f.(72, 0.25)
	
}.fork

