s.waitForBoot{b = Tala.new};
s.waitForBoot{a = PatternPlayer.new};

TalaGUI.initClass

b.tGUI.bounds.height
w = Window("Volume", Rect(0,0,40,300)).front;
x = CompositeView(w, 40@300).background_(Color.red);
x.addFlowLayout.margin_(TalaGUI.m_point).gap_(TalaGUI.m_point/2);


g=EZSlider(
	x, 
	30@265,
	" Vol  ", 
	ControlSpec(-inf, 6, 'db', 0.01, -inf, " dB"),
	{|ez| ez.value.postln},
	initVal:1,
	unitWidth:30, 
	numberWidth:60,
	layout:\vert
).setColors(Color.grey,Color.white, Color.grey(0.7),Color.grey, 
	Color.white, Color.white,nil,nil, Color.grey(0.7))
.font_(Font("Helvetica",10));

b = Button(x, 30@25)
	.states_([
		["M", Color.black, Color.blue(1.8)],
		["M", Color.white, Color.blue(0.8)]
	]);
	
	
w = Window("HI", Rect(0,0,500,500)).front
w.view.background_(Color.blue(1.8))
w.view.background_(Color.blue(0.8))
	
	
	
10/(2+5)@10

4

TalaGUI.margin 					
TalaGUI.m_point 				
TalaGUI.line_height				
TalaGUI.item_extent.x*2
TalaGUI.volume_width			
TalaGUI.item_and_label_extent	
TalaGUI.side_extent.x*2				
TalaGUI.extent					

TalaGUI.side_extent
TalaGUI.line_extent
= 5;
= margin@margin;
= 20;
= 164 @ line_height;
= 40;
= item_extent.x*2@ line_height;
= item_and_label_extent.x + (margin*2)@300;
= (side_extent.x*2)@side_extent.y + (margin*2); 


e = 700@300
m = 5@5;
~se = e.x/2@e.y;

w = Window("full", Rect(0,0,e.x,e.y)).front;
w.addFlowLayout(0@0,0@0);
w.view.background_(Color.blue);
l = CompositeView(w, ~se).background_(Color.red);
l.addFlowLayout(m,m);
r = CompositeView(w, ~se).background_(Color.green);
r.addFlowLayout(m,m);

~label_width = l.bounds.width/2 - (m.x*2);
~line_height = 20;

~tempo = EZNumber(
	l, 
	~se.x-(m.x*2) @ 20,
	" Tempo ",
	ControlSpec(1,999,\lin,1,120,"bpm"),
	initVal:60,
	labelWidth:~label_width,
	gap:m
).setColors(Color.grey, Color.white);

~pop = EZPopUpMenu(
	l,
	~se.x-(m.x*2)@20,
	 " Gati ",
	[
		'3 - Tisra'		->	{|a| },
		'4 - Caturasra'	->	{|a| },
		'5 - Khanda'	->	{|a| },
		'7 - Misra'		->	{|a| },
		'9 - Sankirna'	->	{|a| },
	],
	initVal:1,
	initAction:false,
	labelWidth:~label_width,
	gap:m
).setColors(Color.grey, Color.white);


w.view.decorator.nextLine;


5 * (5+2)

TalaGUI.line_extent
.y - (TalaGUI.margin.x*2) - (3*(TalaGUI.line_extent.y+TalaGUI.margin.x))

b.tGUI.left_side