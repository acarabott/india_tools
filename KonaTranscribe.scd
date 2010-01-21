d = Document("KonaTranscribe")
	.string_("");

w = Window("hi", Rect(400,400,400,400)).front;
g = 4;

w.view.keyDownAction_({|...args|
	var x = args.last;
	var m = args[2];
	var w;
	var set = {|string, jats|
		w = string;
		d.string = d.string + string;
		u = string;
		jathis = jats;
	};
	var speed;
	var jathis;
	var words = List[];
	var shift = if(m==131330){true}{false};
	d.editable = true;
	d.string = d.string[0..d.string.size-2];
	x.postln;
	case
		// Phrases
		{[18,12,0,6].includes(x)}	{if(shift){set.("Dim", 1)}{set.("Ta", 1)}}
		{[19,13,1,7].includes(x)}	{if(shift){set.("Dim -", 2)}{set.("Taka", 2)}}
		{[20,14,2,8].includes(x)}	{if(shift){set.("Dim - -", 3)}{set.("Ta ki ta", 3)}}
		{[21,15,3,9].includes(x)}	{if(shift){set.("Dim - - -", 4)}{set.("Ta ka di mi", 4)}}
		{[23,17,5,11].includes(x)}	{if(shift){set.("Dim - - - -", 5)}{set.("Da di gi na dom", 5)}}
		{[22,16,4,45].includes(x)}	{if(shift){set.("Dim - - - - -", 6)}{set.("Ta ki ta ta ki ta", 6)}}
		{[26,32,38,46].includes(x)}	{if(shift){set.("Dim - - - - - -", 7)}{set.("Ta ka di mi ta ki ta", 7)}}
		{[28,34,40,43].includes(x)}	{if(shift){set.("Dim - - - - - - -", 8)}{set.("Ta ka di mi ta ka ju na", 8)}}
		{[25,31,37,47].includes(x)}	{if(shift){set.("Dim - - - - - - - -", 9)}{set.("Ta ka di mi da di gi na dom", 9)}}		
		{[29,35,41,44].includes(x)}	{set.("-")}		
		//Editing (Delete, return etc)
		{x==36} {
			d.string = d.string + "\n";
		}
		{x==51}						{
			d.string = d.string[0..d.string.findBackwards(" ", true, d.string.size-3)];
			if(d.string.size==1) {
				d.string = "";
			};
		}
		{x==117}					{
			if(u!=nil) {
				d.string = d.string + u;
				u = nil;
			};
		}
		//Gati Changes
		{x==27}	{g = 3}
		{x==24}	{g = 4}
		{x==33}	{g = 5}
		{x==30} {g = 7}
		{x==39} {g = 9};
		("Gati:" + g).postln;
		case
			{[18,19,20,21,23,22,26,28,25,29].includes(x)}	{speed = 1}
			{[12,13,14,15,17,16,32,34,31,35].includes(x)}	{speed = 2}
			{[0,1,2,3,5,4,38,40,37,41].includes(x)}			{speed = 3}
			{[6,7,8,9,11,45,46,43,47,44].includes(x)}		{speed = 4};
		("Speed:" + speed).postln;
		"jathis: ".post; jathis.postln;
	d.string = d.string ++ " ";
	d.selectRange(d.string.size-1,1);
	d.editable = false;
})

/*
	TODO Delete selected text
	TODO Delete from current place... (from current position method)
	TODO Undo/Redo
	TODO Conversion to KonaWords
	TODO Embed in GUI
	TODO Duration indication
	TODO Holding shift changes Takita to Ta--
*/

x = KonaWord.new(4,4,1)
x.play
x.word.do { |item, i|
	if(i!=0) {
		item.asString.toLower.post;
	} {
		item.asString.post;
	};
};


