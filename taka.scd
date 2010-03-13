s.waitForBoot{a = PatternPlayer.new};


x = false;

if(x) {
	fork{
		1.wait;
	}
};
"hi".postln;

fork {
	if(x) {
		1.wait;
	};
	"hi".postln;
}