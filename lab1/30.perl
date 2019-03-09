while (<>){
	s/(a([^a]|([a][^a]))*a){2}a[^a]*a/bad/g;
    print;
}