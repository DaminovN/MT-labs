while (<>){
	s/\(([^)]*)\)/\(\)/g;
    print;
}