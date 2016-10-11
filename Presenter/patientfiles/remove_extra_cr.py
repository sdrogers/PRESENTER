import sys

if __name__ == '__main__':
	infile = sys.argv[1]
	outfile = sys.argv[2]
	with open(infile,'r') as f:
		with open(outfile,'w') as g:
			for line in f:
				print line.strip()
				g.write(line.strip()+'\n')
