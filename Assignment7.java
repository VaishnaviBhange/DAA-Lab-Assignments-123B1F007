//NAME:Vaishnavi Dada Bhange
//PRN:123B1F007
//DATE:3-10-25


import java.util.*;

public class Assignment7 {

    static class UniversityExamScheduler {

        private int totalCourses;
        private List<Integer>[] conflictGraph;

        public UniversityExamScheduler(int totalCourses) {
            this.totalCourses = totalCourses;
            conflictGraph = new ArrayList[totalCourses];
            for (int i = 0; i < totalCourses; i++) {
                conflictGraph[i] = new ArrayList<>();
            }
        }

        public void addConflict(int courseA, int courseB) {
            conflictGraph[courseA].add(courseB);
            conflictGraph[courseB].add(courseA);
        }

        public int[] scheduleExamsGreedy() {
            int[] examSlot = new int[totalCourses];
            Arrays.fill(examSlot, -1);
            boolean[] slotAvailable = new boolean[totalCourses];
            examSlot[0] = 0;

            for (int course = 1; course < totalCourses; course++) {
                Arrays.fill(slotAvailable, true);

                for (int conflict : conflictGraph[course]) {
                    if (examSlot[conflict] != -1) slotAvailable[examSlot[conflict]] = false;
                }

                for (int slot = 0; slot < totalCourses; slot++) {
                    if (slotAvailable[slot]) {
                        examSlot[course] = slot;
                        break;
                    }
                }
            }
            return examSlot;
        }

        public int[] scheduleExamsWelshPowell() {
            int[] examSlot = new int[totalCourses];
            Arrays.fill(examSlot, -1);

            Integer[] courses = new Integer[totalCourses];
            for (int i = 0; i < totalCourses; i++) courses[i] = i;

            Arrays.sort(courses, (a, b) -> conflictGraph[b].size() - conflictGraph[a].size());

            for (int course : courses) {
                boolean[] slotAvailable = new boolean[totalCourses];
                Arrays.fill(slotAvailable, true);

                for (int conflict : conflictGraph[course]) {
                    if (examSlot[conflict] != -1) slotAvailable[examSlot[conflict]] = false;
                }

                for (int slot = 0; slot < totalCourses; slot++) {
                    if (slotAvailable[slot]) {
                        examSlot[course] = slot;
                        break;
                    }
                }
            }
            return examSlot;
        }

        public int[] scheduleExamsDSATUR() {
            int[] examSlot = new int[totalCourses];
            Arrays.fill(examSlot, -1);

            int[] saturation = new int[totalCourses];
            int[] degree = new int[totalCourses];
            for (int i = 0; i < totalCourses; i++) degree[i] = conflictGraph[i].size();

            for (int step = 0; step < totalCourses; step++) {
                int maxSat = -1, selectedCourse = -1;
                for (int course = 0; course < totalCourses; course++) {
                    if (examSlot[course] == -1) {
                        if (saturation[course] > maxSat ||
                                (saturation[course] == maxSat && (selectedCourse == -1 || degree[course] > degree[selectedCourse]))) {
                            maxSat = saturation[course];
                            selectedCourse = course;
                        }
                    }
                }

                boolean[] slotAvailable = new boolean[totalCourses];
                Arrays.fill(slotAvailable, true);

                for (int conflict : conflictGraph[selectedCourse]) {
                    if (examSlot[conflict] != -1) slotAvailable[examSlot[conflict]] = false;
                }

                for (int slot = 0; slot < totalCourses; slot++) {
                    if (slotAvailable[slot]) {
                        examSlot[selectedCourse] = slot;
                        break;
                    }
                }

                for (int conflict : conflictGraph[selectedCourse]) {
                    if (examSlot[conflict] == -1) saturation[conflict]++;
                }
            }
            return examSlot;
        }

        public Map<Integer, List<Integer>> allocateRooms(int[] examSlot, int roomsPerSlot) {
            Map<Integer, List<Integer>> slotToCourses = new HashMap<>();

            for (int course = 0; course < examSlot.length; course++) {
                int slot = examSlot[course];
                slotToCourses.putIfAbsent(slot, new ArrayList<>());

                if (slotToCourses.get(slot).size() < roomsPerSlot) {
                    slotToCourses.get(slot).add(course);
                } else {
                    slot++;
                    slotToCourses.putIfAbsent(slot, new ArrayList<>());
                    slotToCourses.get(slot).add(course);
                }
            }
            return slotToCourses;
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        int totalCourses = 6;
        UniversityExamScheduler scheduler = new UniversityExamScheduler(totalCourses);

        scheduler.addConflict(0, 1);
        scheduler.addConflict(0, 2);
        scheduler.addConflict(1, 3);
        scheduler.addConflict(2, 3);
        scheduler.addConflict(3, 4);
        scheduler.addConflict(4, 5);

        int[] greedySlots = scheduler.scheduleExamsGreedy();
        System.out.println("Greedy Exam Slots: " + Arrays.toString(greedySlots));

        int[] wpSlots = scheduler.scheduleExamsWelshPowell();
        System.out.println("Welsh-Powell Exam Slots: " + Arrays.toString(wpSlots));

        int[] dsaturSlots = scheduler.scheduleExamsDSATUR();
        System.out.println("DSATUR Exam Slots: " + Arrays.toString(dsaturSlots));

        Map<Integer, List<Integer>> rooms = scheduler.allocateRooms(dsaturSlots, 2);
        System.out.println("\nRoom Allocation per Slot:");
        for (int slot : rooms.keySet()) {
            System.out.println("Exam Slot " + slot + ": Courses " + rooms.get(slot));
        }

        long endTime = System.currentTimeMillis();
        System.out.println("\nExecution Time: " + (endTime - startTime) + " ms");
    }
}


