import com.horstmann.adventofcode.*;

record Block(int id, int start, int length, int free) {
    long checksum() {
        long sum = 0;
        for (int i = 0; i < length; i++) 
            sum += id * (start + i);
        return sum;
    }
}

List<Block> blocks;
int pos;
long sum;

void parse(Path p) throws IOException {
    String input = Files.readString(p);
    blocks = new ArrayList<>();
    int start = 0;
    for (int i = 0; i < input.length(); i += 2) {
        int length = input.charAt(i) - '0';
        if (i == input.length() - 1) blocks.add(new Block(i / 2, start, length, 0));
        else {
            int free = input.charAt(i + 1) - '0';
            blocks.add(new Block(i / 2, start, length, free));
            start += length + free;
        }
    }
}

void checksum(int id, int length) {
    for (int i = 0; i < length; i++)
        sum += (pos + i) * id;
    pos += length;
}

Object part1() {
    sum = 0;
    pos = 0;
    int f = 0;
    int b = blocks.size() - 1;
    Block back = blocks.get(b);
    int frontFree = 0;
    int backLength = back.length;
    
    while (f < b) {
        Block front = blocks.get(f);
        frontFree = front.free;
        checksum(front.id, front.length);
        // fill free area 
        boolean filled = false;
        while (!filled) {
            if (backLength <= frontFree) { // back fits
                checksum(back.id, backLength);
                if (backLength < frontFree) { // space for more
                    frontFree -= backLength; 
                }
                else { // fits exactly
                    filled = true;
                }
                b--;
                if (b == f) return sum;
                else {
                    back = blocks.get(b);
                    backLength = back.length;
                }
            } else { // back too large
                checksum(back.id, frontFree);
                backLength -= frontFree;
                filled = true;
            }
        }
        f++;
    }
    checksum(back.id, backLength);
    return sum;
}

Object part2() {
    int b = blocks.size() - 1;
    int lastMoved = b + 1;
    while (b > 0) {
        Block back = blocks.get(b);        
        boolean moved = false;        
        if (back.id() < lastMoved) { // Don't move twice
            for (int f = 0; !moved && f < b; f++) {
                // Find first free space that can hold the entire file
                Block front = blocks.get(f);
                if (front.free >= back.length) { 
                    blocks.remove(b);
                    lastMoved = b;
                    blocks.set(f, new Block(front.id, front.start, front.length, 0));
                    blocks.add(f + 1, new Block(back.id, front.start + front.length, back.length, front.free - back.length));
                    moved = true;
                }
            }
        }        
        if (!moved) b--; // leave at its place
    }    
    
    return blocks.stream().mapToLong(Block::checksum).sum();
}

void main() throws IOException {
    long start = System.nanoTime();
    parse(Util.inputPath("a"));
    IO.println(part1());
    IO.println(part2());
    parse(Util.inputPath("z"));
    IO.println(part1());
    IO.println(part2());
    IO.println("%.3f sec".formatted((System.nanoTime() - start) / 1E9));
}