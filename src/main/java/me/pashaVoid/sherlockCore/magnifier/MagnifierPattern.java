package me.pashaVoid.sherlockCore.magnifier;

public enum MagnifierPattern {
    // Обычный
    COMMON(
            10,   // magnifier_nicks
            90,    // magnifier_break_chances (%)
            1,     // magnifier_show_time (0/1)
            0,     // magnifier_add_chances (%)
            0      // magnifier_show_thief (0/1)
    ),

    // Профессиональный
    PROFESSIONAL(
            15,    // показывает 15 ников
            75,    // 75% шанс износа
            5,
            15,    // +15% к шансам
            1      // подсвечивает воров
    ),

    // Особый
    SPECIAL(
            30,
            30,
            15,
            15,
            1
    ),

    // Уникальный
    UNIQUE(
            50,
            20,
            30,
            20,
            1
    );

    private final int nicks;
    private final int breakChance;
    private final int showTime;
    private final int addChance;
    private final int showThief;

    public static MagnifierPattern getPatternByName(String patternName) {
        if (patternName == null || patternName.isEmpty()) {
            return null;
        }

        String upperName = patternName.toUpperCase().trim();

        for (MagnifierPattern pattern : MagnifierPattern.values()) {
            if (pattern.name().equals(upperName)) {
                return pattern;
            }
        }
        return null;
    }

    MagnifierPattern(int nicks, int breakChance,
                     int showTime, int addChance, int showThief) {
        this.nicks = nicks;
        this.breakChance = breakChance;
        this.showTime = showTime;
        this.addChance = addChance;
        this.showThief = showThief;
    }

    public int getMagnifierNicks() {
        return nicks;
    }


    public int getMagnifierBreakChance() {
        return breakChance;
    }

    public int getMagnifierShowTime() {
        return showTime;
    }

    public int getMagnifierAddChance() {
        return addChance;
    }

    public int getMagnifierShowThief() {
        return showThief;
    }


}