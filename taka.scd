s.waitForBoot {a = PatternPlayer.new};

a.pattern = "xooo xooo"
a.tala.play

a.tala.gati = 3
a.tala.tempo = 90

"xooo oxoo"[0]

"xo asdfhkahdjf ahjsdfhs  oox".reject({|item, i| ['x','o'].includes(item.asSymbol).not })