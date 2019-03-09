$var = 0;
while (<>){
	s/<[^>]*>//g;
	if (/^\s*$/) {
		if ($var == 0) {
			next;
		}
		$var = 2;
		next;
	}
	if ($var == 2) {
		print "\n";
	}
	$var = 1;
	s/^[ \t]+//g;
	s/[ \t]+$//g;
	s/[ \t]+/ /g;
    print;
}