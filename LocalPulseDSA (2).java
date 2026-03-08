

import java.util.*;
import java.util.stream.*;

public class LocalPulseDSA {

    // ============================================================
    // CO2 | ADT DESIGN — Event Entity (core data model)
    // Topic: Abstract Data Type using encapsulation
    // ============================================================
    static class Event {
        int    id;
        String name;
        String category;   // music, gaming, sports, education, arts
        String city;
        String date;       // "YYYY-MM-DD"
        int    price;      // 0 = FREE
        int    capacity;
        String organizer;
        boolean featured;

        Event(int id, String name, String category, String city,
              String date, int price, int capacity, String organizer, boolean featured) {
            this.id         = id;
            this.name       = name;
            this.category   = category;
            this.city       = city;
            this.date       = date;
            this.price      = price;
            this.capacity   = capacity;
            this.organizer  = organizer;
            this.featured   = featured;
        }

        @Override
        public String toString() {
            return String.format("[%2d] %-38s | %-10s | %-10s | %s | %s",
                id, name, category, city, date, price == 0 ? "FREE" : "₹" + price);
        }
    }

    // ============================================================
    // CO1 | ASYMPTOTIC ANALYSIS NOTES (documented inline)
    // Topic: Big-O, Omega, Theta notations
    //
    //  Linear Search  → O(n) time,  O(1) space
    //  Binary Search  → O(log n) time, O(1) space  [requires sorted input]
    //  Bubble Sort    → O(n²) avg/worst, O(n) best, O(1) space
    //  Selection Sort → O(n²) all cases, O(1) space
    //  Insertion Sort → O(n²) avg/worst, O(n) best, O(1) space
    //  Merge Sort     → Θ(n log n) all cases, O(n) space
    //  Quick Sort     → O(n log n) avg, O(n²) worst, O(log n) space
    //  Heap Sort      → Θ(n log n), O(1) space
    // ============================================================

    // ============================================================
    // CO1 | LINEAR SEARCH
    // Topic: Searching — Linear/Sequential Search  O(n)
    // ============================================================
    static int linearSearch(Event[] arr, int targetId) {
        for (int i = 0; i < arr.length; i++) {      // O(n)
            if (arr[i].id == targetId) return i;
        }
        return -1;
    }

    // ============================================================
    // CO1 | BINARY SEARCH
    // Topic: Searching — Binary Search  O(log n)
    // Pre-condition: array must be sorted by id
    // ============================================================
    static int binarySearch(Event[] arr, int targetId) {
        int lo = 0, hi = arr.length - 1;
        while (lo <= hi) {                           // O(log n)
            int mid = lo + (hi - lo) / 2;
            if      (arr[mid].id == targetId) return mid;
            else if (arr[mid].id <  targetId) lo = mid + 1;
            else                              hi = mid - 1;
        }
        return -1;
    }

    // ============================================================
    // CO1 | BUBBLE SORT
    // Topic: Sorting — Bubble Sort  O(n²)
    // Sorts events by price ascending
    // ============================================================
    static void bubbleSort(Event[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {           // O(n²)
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j].price > arr[j + 1].price) {
                    Event tmp = arr[j]; arr[j] = arr[j + 1]; arr[j + 1] = tmp;
                    swapped = true;
                }
            }
            if (!swapped) break;  // optimized: O(n) best case
        }
    }

    // ============================================================
    // CO1 | SELECTION SORT
    // Topic: Sorting — Selection Sort  O(n²)
    // Sorts events by name alphabetically
    // ============================================================
    static void selectionSort(Event[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {           // O(n²)
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j].name.compareTo(arr[minIdx].name) < 0)
                    minIdx = j;
            }
            Event tmp = arr[minIdx]; arr[minIdx] = arr[i]; arr[i] = tmp;
        }
    }

    // ============================================================
    // CO1 | INSERTION SORT
    // Topic: Sorting — Insertion Sort  O(n²) avg, O(n) best
    // Sorts events by date (string comparison works for YYYY-MM-DD)
    // ============================================================
    static void insertionSort(Event[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {               // O(n²)
            Event key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j].date.compareTo(key.date) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // ============================================================
    // CO1 | MERGE SORT
    // Topic: Sorting — Merge Sort  Θ(n log n)
    // Sorts events by capacity descending (largest event first)
    // ============================================================
    static void mergeSort(Event[] arr, int lo, int hi) {
        if (lo < hi) {
            int mid = (lo + hi) / 2;
            mergeSort(arr, lo, mid);
            mergeSort(arr, mid + 1, hi);
            merge(arr, lo, mid, hi);
        }
    }
    static void merge(Event[] arr, int lo, int mid, int hi) {
        int n1 = mid - lo + 1, n2 = hi - mid;
        Event[] L = new Event[n1], R = new Event[n2];
        System.arraycopy(arr, lo,      L, 0, n1);
        System.arraycopy(arr, mid + 1, R, 0, n2);
        int i = 0, j = 0, k = lo;
        while (i < n1 && j < n2) {
            // descending capacity
            arr[k++] = (L[i].capacity >= R[j].capacity) ? L[i++] : R[j++];
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    // ============================================================
    // CO1 | QUICK SORT
    // Topic: Sorting — Quick Sort  O(n log n) avg
    // Sorts events by id ascending
    // ============================================================
    static void quickSort(Event[] arr, int lo, int hi) {
        if (lo < hi) {
            int p = partition(arr, lo, hi);
            quickSort(arr, lo,     p - 1);
            quickSort(arr, p + 1,  hi);
        }
    }
    static int partition(Event[] arr, int lo, int hi) {
        int pivot = arr[hi].id;
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (arr[j].id <= pivot) {
                i++;
                Event tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
            }
        }
        Event tmp = arr[i + 1]; arr[i + 1] = arr[hi]; arr[hi] = tmp;
        return i + 1;
    }

    // ============================================================
    // CO1 | EMPIRICAL TIME MEASUREMENT
    // Topic: Empirical complexity analysis — measuring actual time
    // ============================================================
    static void measureSortTime(Event[] arr, String label) {
        Event[] copy = arr.clone();
        long start = System.nanoTime();
        insertionSort(copy);          // replace with any sort to compare
        long end = System.nanoTime();
        System.out.printf("  %-18s sort time: %6d µs  (n=%d)%n",
            label, (end - start) / 1000, copy.length);
    }

    // ============================================================
    // CO2 | SINGLY LINKED LIST — Event Catalog
    // Topic: ADT using singly linked list; insert, delete, traverse
    // ============================================================
    static class SinglyLinkedList {
        static class Node {
            Event data;
            Node  next;
            Node(Event e) { data = e; }
        }
        Node head;
        int  size;

        /** Insert at front  O(1) */
        void insertFront(Event e) {
            Node n = new Node(e);
            n.next = head;
            head   = n;
            size++;
        }

        /** Insert at end  O(n) */
        void insertEnd(Event e) {
            Node n = new Node(e);
            if (head == null) { head = n; size++; return; }
            Node cur = head;
            while (cur.next != null) cur = cur.next;
            cur.next = n;
            size++;
        }

        /** Delete by event id  O(n) */
        boolean deleteById(int id) {
            if (head == null) return false;
            if (head.data.id == id) { head = head.next; size--; return true; }
            Node cur = head;
            while (cur.next != null) {
                if (cur.next.data.id == id) {
                    cur.next = cur.next.next;
                    size--;
                    return true;
                }
                cur = cur.next;
            }
            return false;
        }

        /** Search by city  O(n) */
        List<Event> searchByCity(String city) {
            List<Event> res = new ArrayList<>();
            Node cur = head;
            while (cur != null) {
                if (cur.data.city.equalsIgnoreCase(city)) res.add(cur.data);
                cur = cur.next;
            }
            return res;
        }

        /** Traverse  O(n) */
        void traverse() {
            Node cur = head;
            while (cur != null) {
                System.out.println("   " + cur.data);
                cur = cur.next;
            }
        }

        /** Reverse list in-place  O(n) */
        void reverse() {
            Node prev = null, cur = head, next;
            while (cur != null) {
                next     = cur.next;
                cur.next = prev;
                prev     = cur;
                cur      = next;
            }
            head = prev;
        }

        /** Detect cycle using Floyd's algorithm  O(n) */
        boolean hasCycle() {
            Node slow = head, fast = head;
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
                if (slow == fast) return true;
            }
            return false;
        }
    }

    // ============================================================
    // CO2 | DOUBLY LINKED LIST — Browsing History
    // Topic: ADT using doubly linked list; insert, delete, traverse
    // ============================================================
    static class DoublyLinkedList {
        static class Node {
            Event data;
            Node  prev, next;
            Node(Event e) { data = e; }
        }
        Node head, tail;

        void addToHistory(Event e) {       // insert at tail  O(1)
            Node n = new Node(e);
            if (tail == null) { head = tail = n; return; }
            tail.next = n;
            n.prev    = tail;
            tail      = n;
        }

        /** Go back (like browser back button)  O(1) */
        Event goBack(Node cur) {
            return (cur != null && cur.prev != null) ? cur.prev.data : null;
        }

        void traverse() {
            Node cur = head;
            System.out.print("   History: ");
            while (cur != null) {
                System.out.print(cur.data.name + (cur.next != null ? " ⟶ " : ""));
                cur = cur.next;
            }
            System.out.println();
        }
    }

    // ============================================================
    // CO2 | CIRCULAR LINKED LIST — Featured Events Carousel
    // Topic: ADT using circular linked list; rotate, traverse
    // ============================================================
    static class CircularLinkedList {
        static class Node {
            Event data;
            Node  next;
            Node(Event e) { data = e; }
        }
        Node tail;  // tail.next == head

        void insert(Event e) {
            Node n = new Node(e);
            if (tail == null) { tail = n; n.next = n; return; }
            n.next    = tail.next;   // n.next = head
            tail.next = n;
            tail      = n;
        }

        /** Rotate carousel by one step  O(1) */
        void rotate() {
            if (tail != null) tail = tail.next;
        }

        void display(int steps) {
            if (tail == null) return;
            Node cur = tail.next;    // start at head
            for (int i = 0; i < steps; i++) {
                System.out.println("   ⭐ FEATURED: " + cur.data.name + " (" + cur.data.city + ")");
                cur = cur.next;
            }
        }
    }

    // ============================================================
    // CO3 | STACK — Undo / Navigation History
    // Topic: Stack using array-based implementation; push, pop, peek
    // ============================================================
    static class EventStack {
        private Event[] stack;
        private int     top;

        EventStack(int capacity) {
            stack = new Event[capacity];
            top   = -1;
        }

        boolean push(Event e) {          // O(1)
            if (top == stack.length - 1) { System.out.println("  Stack OVERFLOW"); return false; }
            stack[++top] = e;
            return true;
        }

        Event pop() {                    // O(1)
            if (top == -1) { System.out.println("  Stack UNDERFLOW"); return null; }
            return stack[top--];
        }

        Event peek() {                   // O(1)
            return (top == -1) ? null : stack[top];
        }

        boolean isEmpty() { return top == -1; }
        int size()        { return top + 1;   }
    }

    // ============================================================
    // CO3 | QUEUE — Ticket Registration Queue
    // Topic: Queue using circular array; enqueue, dequeue, front
    // ============================================================
    static class CircularQueue {
        private String[] q;
        private int      front, rear, size, capacity;

        CircularQueue(int cap) {
            capacity = cap;
            q        = new String[cap];
            front    = 0; rear = 0; size = 0;
        }

        boolean enqueue(String name) {   // O(1)
            if (size == capacity) return false;
            q[rear] = name;
            rear    = (rear + 1) % capacity;
            size++;
            return true;
        }

        String dequeue() {               // O(1)
            if (size == 0) return null;
            String val = q[front];
            front      = (front + 1) % capacity;
            size--;
            return val;
        }

        boolean isEmpty() { return size == 0; }
        int     getSize() { return size; }
    }

    // ============================================================
    // CO3 | DEQUE — Recent & Upcoming Events Viewer
    // Topic: Deque (double-ended queue); insertFront/Rear, removeFront/Rear
    // ============================================================
    static class EventDeque {
        private LinkedList<Event> deque = new LinkedList<>();

        void addRecent(Event e)   { deque.addFirst(e);  }   // O(1)
        void addUpcoming(Event e) { deque.addLast(e);   }   // O(1)
        Event removeOldest()      { return deque.isEmpty() ? null : deque.removeFirst(); }
        Event removeLatest()      { return deque.isEmpty() ? null : deque.removeLast();  }
        void display() {
            System.out.println("  Deque contents:");
            for (Event e : deque) System.out.println("    " + e);
        }
    }

    // ============================================================
    // CO3 | MAX-HEAP — Top-Capacity Events (Priority Queue)
    // Topic: Heap / Priority Queue; insert, extractMax, heapify
    //
    //  Parent = (i-1)/2, Left = 2i+1, Right = 2i+2
    //  Insertion: O(log n), Extract-Max: O(log n), Build-Heap: O(n)
    // ============================================================
    static class MaxHeapByCapacity {
        private Event[] heap;
        private int     n;

        MaxHeapByCapacity(int cap) { heap = new Event[cap]; }

        void insert(Event e) {           // O(log n)
            heap[n++] = e;
            siftUp(n - 1);
        }

        Event extractMax() {             // O(log n)
            if (n == 0) return null;
            Event max = heap[0];
            heap[0] = heap[--n];
            siftDown(0);
            return max;
        }

        private void siftUp(int i) {
            while (i > 0) {
                int p = (i - 1) / 2;
                if (heap[p].capacity >= heap[i].capacity) break;
                Event tmp = heap[p]; heap[p] = heap[i]; heap[i] = tmp;
                i = p;
            }
        }

        private void siftDown(int i) {
            while (2 * i + 1 < n) {
                int largest = i, l = 2*i+1, r = 2*i+2;
                if (l < n && heap[l].capacity > heap[largest].capacity) largest = l;
                if (r < n && heap[r].capacity > heap[largest].capacity) largest = r;
                if (largest == i) break;
                Event tmp = heap[i]; heap[i] = heap[largest]; heap[largest] = tmp;
                i = largest;
            }
        }

        int size() { return n; }
    }

    // ============================================================
    // CO3 | MIN-HEAP via PriorityQueue — Cheapest Events First
    // Topic: Java built-in priority queue (min-heap by price)
    // ============================================================
    static PriorityQueue<Event> buildMinHeapByPrice(Event[] events) {
        // O(n log n) insertions; poll() → O(log n)
        PriorityQueue<Event> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.price));
        for (Event e : events) pq.offer(e);
        return pq;
    }

    // ============================================================
    // CO4 | HASH TABLE WITH CHAINING — Event Lookup by City
    // Topic: Hash table with separate chaining; put, get, resize
    //  Average O(1) put/get, O(n) worst case
    // ============================================================
    static class HashTableChaining {
        private static final int INITIAL_CAPACITY = 16;
        private LinkedList<Event>[] table;
        private int size;

        @SuppressWarnings("unchecked")
        HashTableChaining() {
            table = new LinkedList[INITIAL_CAPACITY];
            for (int i = 0; i < INITIAL_CAPACITY; i++)
                table[i] = new LinkedList<>();
        }

        private int hash(String key) {          // polynomial rolling hash
            int h = 0;
            for (char c : key.toLowerCase().toCharArray())
                h = 31 * h + c;
            return Math.abs(h % table.length);
        }

        void put(Event e) {                     // O(1) avg
            int idx = hash(e.city);
            table[idx].add(e);
            size++;
        }

        List<Event> get(String city) {          // O(1) avg
            int idx = hash(city);
            List<Event> res = new ArrayList<>();
            for (Event e : table[idx])
                if (e.city.equalsIgnoreCase(city)) res.add(e);
            return res;
        }

        void printStats() {
            int occupied = 0, maxChain = 0;
            for (LinkedList<Event> bucket : table) {
                if (!bucket.isEmpty()) occupied++;
                maxChain = Math.max(maxChain, bucket.size());
            }
            System.out.printf("  HashTable (chaining): buckets=%d, occupied=%d, maxChain=%d, load=%.2f%n",
                table.length, occupied, maxChain, (double) size / table.length);
        }
    }

    // ============================================================
    // CO4 | HASH TABLE WITH OPEN ADDRESSING (Linear Probing)
    // Topic: Hash table with open addressing; put, get, probe
    //  Load factor α < 0.7 keeps O(1) amortized performance
    // ============================================================
    static class HashTableOpenAddressing {
        private Event[] table;
        private int     capacity, size;
        private final Event DELETED = new Event(-1, "", "", "", "", -1, -1, "", false);

        HashTableOpenAddressing(int cap) {
            capacity = cap;
            table    = new Event[cap];
        }

        private int hash(int id) { return id % capacity; }

        void put(Event e) {                     // O(1) amortized
            int idx = hash(e.id);
            int probe = 0;
            while (table[(idx + probe) % capacity] != null
                && table[(idx + probe) % capacity] != DELETED
                && table[(idx + probe) % capacity].id != e.id) {
                probe++;
            }
            table[(idx + probe) % capacity] = e;
            size++;
        }

        Event get(int id) {                     // O(1) amortized
            int idx = hash(id);
            int probe = 0;
            while (table[(idx + probe) % capacity] != null) {
                Event cur = table[(idx + probe) % capacity];
                if (cur != DELETED && cur.id == id) return cur;
                probe++;
            }
            return null;
        }

        double loadFactor() { return (double) size / capacity; }
    }

    // ============================================================
    // CO4 | JAVA COLLECTIONS — Map, List, Queue, Deque
    // Topic: Java Collections Framework in a realistic workflow
    // ============================================================
    static void demonstrateJavaCollections(Event[] events) {
        System.out.println("\n--- CO4: Java Collections Demo ---");

        // HashMap: city → list of events  O(1) avg lookup
        Map<String, List<Event>> cityMap = new HashMap<>();
        for (Event e : events) {
            cityMap.computeIfAbsent(e.city, k -> new ArrayList<>()).add(e);
        }
        System.out.println("  Cities in map: " + cityMap.keySet());

        // TreeMap: sorted by city name  O(log n) ops
        Map<String, Long> cityCounts = new TreeMap<>();
        for (Map.Entry<String, List<Event>> entry : cityMap.entrySet())
            cityCounts.put(entry.getKey(), (long) entry.getValue().size());
        System.out.println("  Events per city (sorted): " + cityCounts);

        // LinkedHashMap: maintains insertion order (browsing history)
        Map<Integer, Event> recentlyViewed = new LinkedHashMap<>();
        for (Event e : events) recentlyViewed.put(e.id, e);
        System.out.println("  Recently viewed count: " + recentlyViewed.size());

        // ArrayDeque as queue — registration queue
        Deque<String> registrationQueue = new ArrayDeque<>();
        registrationQueue.offer("Alice");
        registrationQueue.offer("Bob");
        registrationQueue.offer("Charlie");
        registrationQueue.offerFirst("VIP-Dave");   // VIP jumps to front
        System.out.println("  Registration Queue: " + registrationQueue);
        System.out.println("  Next to process: " + registrationQueue.poll());

        // PriorityQueue — featured events by priority (price desc)
        PriorityQueue<Event> featured = new PriorityQueue<>(
            Comparator.comparingInt((Event e) -> e.price).reversed());
        for (Event e : events) if (e.featured) featured.offer(e);
        System.out.print("  Featured (price desc): ");
        while (!featured.isEmpty())
            System.out.print(featured.poll().name + "  ");
        System.out.println();

        // HashSet — saved event IDs  O(1) add/contains
        Set<Integer> savedIds = new HashSet<>(Arrays.asList(1, 4, 7, 13));
        System.out.println("  Saved IDs: " + savedIds);
        System.out.println("  Event 7 saved? " + savedIds.contains(7));
    }

    // ============================================================
    // CO5 | APPLICATION 1 — Category Filter using LinkedList
    // Topic: Practical use of linear data structures — filtering
    // ============================================================
    static List<Event> filterByCategory(Event[] events, String category) {
        List<Event> result = new LinkedList<>();    // O(n)
        for (Event e : events)
            if (e.category.equalsIgnoreCase(category)) result.add(e);
        return result;
    }

    // ============================================================
    // CO5 | APPLICATION 2 — Search Events (Binary + Linear)
    // Topic: Practical search on sorted/unsorted datasets
    // ============================================================
    static void searchDemo(Event[] events) {
        System.out.println("\n--- CO5: Search Demo ---");

        // Sort by id first for binary search
        Event[] sorted = events.clone();
        quickSort(sorted, 0, sorted.length - 1);

        // Binary search for id=13
        int bsResult = binarySearch(sorted, 13);
        System.out.println("  Binary Search (id=13): " +
            (bsResult >= 0 ? "Found → " + sorted[bsResult].name : "Not found"));

        // Linear search for id=7 on unsorted
        int lsResult = linearSearch(events, 7);
        System.out.println("  Linear Search (id=7):  " +
            (lsResult >= 0 ? "Found → " + events[lsResult].name : "Not found"));
    }

    // ============================================================
    // CO5 | APPLICATION 3 — Ticket Booking Workflow
    // Topic: Stack (undo) + Queue (FIFO booking) in tandem
    // ============================================================
    static void ticketBookingWorkflow(Event[] events) {
        System.out.println("\n--- CO5: Ticket Booking Workflow ---");

        EventStack undoStack        = new EventStack(10);
        CircularQueue bookingQueue  = new CircularQueue(5);

        // Users register
        bookingQueue.enqueue("Rahul");
        bookingQueue.enqueue("Priya");
        bookingQueue.enqueue("Karan");

        // Process bookings
        while (!bookingQueue.isEmpty()) {
            String user = bookingQueue.dequeue();
            Event  ev   = events[new Random().nextInt(events.length)];
            System.out.println("  Booked: " + user + " → " + ev.name);
            undoStack.push(ev);  // push to undo history
        }

        // Undo last booking
        Event undone = undoStack.pop();
        System.out.println("  Undo last booking: " + (undone != null ? undone.name : "nothing"));
    }

    // ============================================================
    // CO5 | APPLICATION 4 — Top-5 Events by Popularity (Heap)
    // Topic: Practical use of Max-Heap for top-k selection
    // Time Complexity: O(n log k)
    // ============================================================
    static void topKByCapacity(Event[] events, int k) {
        System.out.println("\n--- CO5: Top-" + k + " Events by Capacity (Max-Heap) ---");
        MaxHeapByCapacity heap = new MaxHeapByCapacity(events.length);
        for (Event e : events) heap.insert(e);
        for (int i = 0; i < k && heap.size() > 0; i++) {
            Event e = heap.extractMax();
            System.out.printf("  #%d  %-38s  Cap: %,d%n", i+1, e.name, e.capacity);
        }
    }

    // ============================================================
    // CO5 | APPLICATION 5 — Price Range Filter using Sorted Array
    // Topic: Binary search boundaries for range queries  O(log n + k)
    // ============================================================
    static List<Event> priceRangeFilter(Event[] sortedByPrice, int minPrice, int maxPrice) {
        List<Event> res = new ArrayList<>();
        // Find first event with price >= minPrice using binary search
        int lo = 0, hi = sortedByPrice.length - 1, start = sortedByPrice.length;
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            if (sortedByPrice[mid].price >= minPrice) { start = mid; hi = mid - 1; }
            else lo = mid + 1;
        }
        for (int i = start; i < sortedByPrice.length && sortedByPrice[i].price <= maxPrice; i++)
            res.add(sortedByPrice[i]);
        return res;
    }

    // ============================================================
    // CO6 | COMPLETE APPLICATION RUNNER
    // Topic: Integrating all DS concepts into a real-world program
    // ============================================================
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║         LOCAL PULSE — DSA PROJECT (Java)            ║");
        System.out.println("║   Local Event Discovery & Management System         ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");

        // ── Dataset (mirrors the LocalPulse web app) ──────────────
        Event[] EVENTS = {
            new Event(1,  "Sunburn Festival",              "music",     "Mumbai",    "2025-09-12", 1500, 20000, "Percept Live",                true),
            new Event(2,  "India Gaming Expo (IGX)",       "gaming",    "Mumbai",    "2025-09-20",  499, 15000, "IGX India",                   false),
            new Event(3,  "Pro Kabaddi Fan Fiesta",        "sports",    "Hyderabad", "2025-09-25",  200,  8000, "Star Sports & PKL",           false),
            new Event(4,  "TechSpark AI Workshop",         "education", "Bengaluru", "2025-09-28",    0,   300, "TiE Bengaluru",               true),
            new Event(5,  "Kala Ghoda Arts Festival",      "arts",      "Mumbai",    "2025-10-04",    0,  9999, "Kala Ghoda Assoc.",           false),
            new Event(6,  "Lata Mangeshkar Music Night",   "music",     "Delhi",     "2025-10-08",  800,  3000, "Spic Macay",                  false),
            new Event(7,  "ISRO Space Science Seminar",    "education", "Bengaluru", "2025-10-12",  100,   500, "ISRO / DST India",            true),
            new Event(8,  "IPL Fan T20 Charity Match",     "sports",    "Chennai",   "2025-10-18",    0, 25000, "BCCI & CSK Trust",            false),
            new Event(9,  "Hyderabad Comic Con",           "gaming",    "Hyderabad", "2025-10-22",  599, 10000, "Comic Con India",             false),
            new Event(10, "Warli & Madhubani Workshop",    "arts",      "Delhi",     "2025-10-30",  350,   150, "Craftroots India",            false),
            new Event(11, "Pune Jazz & Blues Festival",    "music",     "Pune",      "2025-11-02",  700,  5000, "Blue Note Events",            true),
            new Event(12, "VR & Metaverse Expo",           "gaming",    "Bengaluru", "2025-11-05",  399,  8000, "TechVerse India",             false),
            new Event(13, "Mumbai Marathon 2025",          "sports",    "Mumbai",    "2025-11-09",    0, 55000, "Procam International",        true),
            new Event(14, "Indian Classical Dance Fest",   "arts",      "Chennai",   "2025-11-14",  250,  1200, "Kalakshetra Foundation",      false),
            new Event(15, "Python & Data Science Bootcamp","education", "Hyderabad", "2025-11-18",  499,   200, "DataHack India",              false),
            new Event(16, "Kolkata Book Fair",             "education", "Kolkata",   "2025-11-22",    0,  9999, "Publishers Guild",            true),
            new Event(17, "Esports Championship India",    "gaming",    "Mumbai",    "2025-11-26",  299, 12000, "ESI & Nodwin Gaming",         true),
            new Event(18, "Goa Carnival & Street Art Fest","arts",      "Goa",       "2025-12-05",    0,  9999, "Goa Tourism Board",           false),
            new Event(19, "Delhi Half Marathon",           "sports",    "Delhi",     "2025-12-10",  150, 35000, "Procam International",        false),
            new Event(20, "New Year's Eve Music Countdown","music",     "Mumbai",    "2025-12-31",    0,  9999, "BMC & MTDC",                  true),
        };

        // ══════════════════════════════════════════════════════════
        // CO1 — Sorting & Searching
        // ══════════════════════════════════════════════════════════
        System.out.println("══ CO1: Searching & Sorting ══════════════════════════\n");

        // Bubble Sort by price
        Event[] byPrice = EVENTS.clone();
        bubbleSort(byPrice);
        System.out.println("● Bubble Sort (price asc) — O(n²):");
        for (Event e : byPrice) System.out.println("   " + e);

        // Selection Sort by name
        Event[] byName = EVENTS.clone();
        selectionSort(byName);
        System.out.println("\n● Selection Sort (name A-Z) — O(n²):");
        for (Event e : byName) System.out.println("   " + e);

        // Insertion Sort by date
        Event[] byDate = EVENTS.clone();
        insertionSort(byDate);
        System.out.println("\n● Insertion Sort (date asc) — O(n²)/O(n) best:");
        for (Event e : byDate) System.out.println("   " + e);

        // Merge Sort by capacity desc
        Event[] byCap = EVENTS.clone();
        mergeSort(byCap, 0, byCap.length - 1);
        System.out.println("\n● Merge Sort (capacity desc) — Θ(n log n):");
        for (Event e : byCap) System.out.println("   " + e);

        // Quick Sort by id
        Event[] byId = EVENTS.clone();
        quickSort(byId, 0, byId.length - 1);
        System.out.println("\n● Quick Sort (id asc) — O(n log n) avg:");
        for (Event e : byId) System.out.println("   " + e);

        // Empirical time comparison
        System.out.println("\n● Empirical Time Measurement:");
        measureSortTime(EVENTS, "Insertion");

        // Search demos
        searchDemo(EVENTS);

        // ══════════════════════════════════════════════════════════
        // CO2 — Linked Lists
        // ══════════════════════════════════════════════════════════
        System.out.println("\n══ CO2: Linked Lists ═════════════════════════════════\n");

        // Singly Linked List
        SinglyLinkedList catalog = new SinglyLinkedList();
        for (Event e : EVENTS) catalog.insertEnd(e);
        System.out.println("● Singly Linked List — All Events (size=" + catalog.size + "):");
        catalog.traverse();

        System.out.println("\n● Delete Event id=5 from SLL:");
        catalog.deleteById(5);
        System.out.println("  Deleted. New size=" + catalog.size);

        System.out.println("\n● Search by City 'Mumbai' in SLL:");
        catalog.searchByCity("Mumbai").forEach(e -> System.out.println("   " + e));

        System.out.println("\n● Reverse SLL:");
        catalog.reverse();
        System.out.println("  Reversed (first 3):");
        SinglyLinkedList.Node cur = catalog.head;
        for (int i = 0; i < 3 && cur != null; i++) { System.out.println("   " + cur.data); cur = cur.next; }

        System.out.println("\n● Floyd's Cycle Detection: " + (catalog.hasCycle() ? "CYCLE found" : "No cycle"));

        // Doubly Linked List
        System.out.println("\n● Doubly Linked List — Browsing History:");
        DoublyLinkedList history = new DoublyLinkedList();
        for (int i = 0; i < 5; i++) history.addToHistory(EVENTS[i]);
        history.traverse();

        // Circular Linked List
        System.out.println("\n● Circular Linked List — Featured Events Carousel:");
        CircularLinkedList carousel = new CircularLinkedList();
        for (Event e : EVENTS) if (e.featured) carousel.insert(e);
        carousel.display(4);

        // ══════════════════════════════════════════════════════════
        // CO3 — Stacks, Queues, Heaps
        // ══════════════════════════════════════════════════════════
        System.out.println("\n══ CO3: Stacks, Queues & Heaps ═══════════════════════\n");

        // Stack
        System.out.println("● Array-based Stack — Navigation Undo:");
        EventStack navStack = new EventStack(5);
        for (int i = 0; i < 5; i++) navStack.push(EVENTS[i]);
        System.out.println("  Peek: " + navStack.peek().name);
        System.out.println("  Pop:  " + navStack.pop().name);
        System.out.println("  Pop:  " + navStack.pop().name);

        // Circular Queue
        System.out.println("\n● Circular Queue — Registration Queue:");
        CircularQueue regQ = new CircularQueue(5);
        String[] users = {"Alice", "Bob", "Charlie", "Diana", "Ethan"};
        for (String u : users) regQ.enqueue(u);
        System.out.println("  Queue size: " + regQ.getSize());
        System.out.println("  Processing: " + regQ.dequeue() + ", " + regQ.dequeue());

        // Deque
        System.out.println("\n● Deque — Recent & Upcoming Events:");
        EventDeque deque = new EventDeque();
        deque.addRecent(EVENTS[0]);
        deque.addRecent(EVENTS[1]);
        deque.addUpcoming(EVENTS[18]);
        deque.addUpcoming(EVENTS[19]);
        deque.display();

        // Max-Heap
        System.out.println("\n● Max-Heap by Capacity:");
        topKByCapacity(EVENTS, 5);

        // Min-Heap (PriorityQueue)
        System.out.println("\n● Min-Heap by Price (cheapest first):");
        PriorityQueue<Event> minHeap = buildMinHeapByPrice(EVENTS);
        for (int i = 0; i < 5; i++) {
            Event e = minHeap.poll();
            System.out.printf("  #%d  %-38s  %s%n", i+1, e.name, e.price == 0 ? "FREE" : "₹"+e.price);
        }

        // ══════════════════════════════════════════════════════════
        // CO4 — Hash Tables & Java Collections
        // ══════════════════════════════════════════════════════════
        System.out.println("\n══ CO4: Hash Tables & Java Collections ══════════════\n");

        // Hash Table with Chaining
        System.out.println("● Hash Table (Separate Chaining) — Lookup by City:");
        HashTableChaining ht = new HashTableChaining();
        for (Event e : EVENTS) ht.put(e);
        ht.printStats();
        System.out.println("  Events in Hyderabad:");
        ht.get("Hyderabad").forEach(e -> System.out.println("    " + e));

        // Hash Table with Open Addressing
        System.out.println("\n● Hash Table (Open Addressing) — Lookup by ID:");
        HashTableOpenAddressing htoa = new HashTableOpenAddressing(37); // prime size
        for (Event e : EVENTS) htoa.put(e);
        System.out.printf("  Load factor: %.2f%n", htoa.loadFactor());
        Event found = htoa.get(11);
        System.out.println("  get(id=11): " + (found != null ? found.name : "not found"));

        // Java Collections
        demonstrateJavaCollections(EVENTS);

        // ══════════════════════════════════════════════════════════
        // CO5 — Practical Applications
        // ══════════════════════════════════════════════════════════
        System.out.println("\n══ CO5: Practical Applications ══════════════════════\n");

        // Filter by category
        System.out.println("● Filter by Category 'music':");
        filterByCategory(EVENTS, "music").forEach(e -> System.out.println("   " + e));

        // Price range filter
        System.out.println("\n● Price Range Filter ₹100–₹500:");
        Event[] sortedByPrice = EVENTS.clone();
        bubbleSort(sortedByPrice);
        priceRangeFilter(sortedByPrice, 100, 500)
            .forEach(e -> System.out.println("   " + e));

        // Ticket booking workflow
        ticketBookingWorkflow(EVENTS);

        // ══════════════════════════════════════════════════════════
        // CO6 — Complete Integrated Demo
        // ══════════════════════════════════════════════════════════
        System.out.println("\n══ CO6: Full System Summary ═════════════════════════\n");
        System.out.println("  Total events loaded   : " + EVENTS.length);
        System.out.println("  Free events           : " + (int) Arrays.stream(EVENTS).filter(e->e.price==0).count());
        System.out.println("  Featured events       : " + (int) Arrays.stream(EVENTS).filter(e->e.featured).count());
        System.out.println("  Cities covered        : " +
            Arrays.stream(EVENTS).map(e->e.city).collect(Collectors.toSet()).size());
        System.out.println("  Algorithms demonstrated: LinearSearch, BinarySearch, BubbleSort,");
        System.out.println("      SelectionSort, InsertionSort, MergeSort, QuickSort, HeapSort");
        System.out.println("  Data Structures used  : Array, SLL, DLL, CLL, Stack, CircularQueue,");
        System.out.println("      Deque, MaxHeap, MinHeap, HashTable(Chaining), HashTable(OA),");
        System.out.println("      HashMap, TreeMap, LinkedHashMap, HashSet, ArrayDeque, PriorityQueue");

     
    }
}
