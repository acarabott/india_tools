w = Window("Lala", Rect(400,400,400,400)).front;
t = TextView(w, Rect(10,10,380,300))

a = List[]

f = {|string|
	a.add(string);
	t.string = "";
	a.do { |item, i|
		t.string = t.string + item 
	};
};

f.("takita")
t.editable = false

SCTextView

t.selectionStart
t.selectionSize