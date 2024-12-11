/* An alternative with "data-oriented programming" */

import com.horstmann.adventofcode.*;

sealed interface Block {
    int length();
}

record FileBlock(int id, int length) implements Block {}
record FreeBlock(int length) implements Block  {}

List<Block> blocks;

void parse(Path p) throws IOException {
    String input = Files.readString(p);
    blocks = new ArrayList<>();
    for (int i = 0; i < input.length(); i ++) {
        int length = input.charAt(i) - '0';
        blocks.add(i % 2 == 0 ? new FileBlock(i / 2, length) : new FreeBlock(length));
    }
}

long checksum(List<Block> blocks) {
    long sum = 0;
    int start = 0;
    for (var b : blocks) {
        switch (b) {
            case FileBlock fb -> { for (int i = 0; i < b.length(); i++) { sum += fb.id() * start; start++; } }
            default -> { start += b.length(); }
        };
    }    
    return sum;
}

Object part1() {
    var defragged = new ArrayList<Block>();
    int b = blocks.size() - 1;
    FileBlock back = (FileBlock) blocks.get(b);
    int backLength = back.length();
    int f = 0;
    int frontFree = 0;
    
    while (f < b) {
        Block front = blocks.get(f);
        defragged.add(front);
        frontFree = blocks.get(f + 1).length();
        boolean filled = false;
        while (!filled) {
            if (backLength <= frontFree) { // back fits
                defragged.add(new FileBlock(back.id(), backLength));
                if (backLength < frontFree) { // space for more
                    frontFree -= backLength; 
                }
                else { // fits exactly                    
                    filled = true;
                }
                b -= 2;
                if (f < b) {
                    back = (FileBlock) blocks.get(b);
                    backLength = back.length();
                } else {
                    filled = true; 
                    backLength = 0;
                }
            } else { // back too large
                defragged.add(new FileBlock(back.id(), frontFree));
                backLength -= frontFree;
                filled = true;
            }
        }
        f += 2;
    }
    if (backLength > 0) defragged.add(new FileBlock(back.id(), backLength));
    return checksum(defragged);
}

Object part2() {
    int b = blocks.size() - 1;
    int lastMoved = b + 1;
    while (b > 0) {
        if (blocks.get(b) instanceof FileBlock back) {        
            boolean moved = false;        
            if (back.id() < lastMoved) { // Don't move twice
                for (int f = 1; !moved && f < b; f++) {
                    // Find first free space that can hold the entire file
                    if (blocks.get(f) instanceof FreeBlock front && front.length() >= back.length()) { 
                        blocks.set(b, new FreeBlock(back.length));
                        lastMoved = back.id();
                        blocks.set(f, back);
                        if (front.length() > back.length()) {
                            blocks.add(f + 1, new FreeBlock(front.length() - back.length()));
                            b++;
                        }
                        moved = true;
                    }
                }
            }        
        }
        b--; 
    }    
    
    return checksum(blocks);
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