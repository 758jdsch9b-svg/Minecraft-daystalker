# Daystalker Mod — Forge 1.20.1

Ett enkelt Forge-mod som lägger till **Daystalker**: ett fientligt monster
som spawnar på dagen och droppar diamanter när det dör.

## Egenskaper

| Egenskap        | Värde                        |
|-----------------|------------------------------|
| Hälsa           | 20 HP (10 hjärtan)           |
| Attack          | 4 (2 hjärtan)                |
| Hastighet       | 0.28 (lite snabbare än zombie)|
| Spawnar         | Dagtid, på marken            |
| Bränner sig     | Nej                          |
| Drops           | 1–2 diamanter (+looting)     |

## Kom igång

### Krav
- JDK 17
- Minecraft Forge 1.20.1 MDK

### Setup
1. Ladda ner [Forge 1.20.1 MDK](https://files.minecraftforge.net/)
2. Lägg in dessa filer i MDK-mappen
3. Kör:
   ```
   ./gradlew genEclipseRuns   (Eclipse)
   ./gradlew genIntellijRuns  (IntelliJ)
   ```
4. Öppna projektet i din IDE

### Bygg
```
./gradlew build
```
JAR-filen hamnar i `build/libs/`

## Textur
Monstret använder zombie-modellen som placeholder.
Lägg in din egen textur här:
```
src/main/resources/assets/daystalker/textures/entity/daystalker.png
```
Rekommenderad storlek: **64x64 px** (samma format som vanilla zombie-textur)

## Filstruktur
```
src/main/java/com/example/daystalker/
├── DaystalkerMod.java          ← Huvud-mod-klass
├── ClientEvents.java           ← Renderer-registrering
├── entity/
│   ├── DaystalkerEntity.java   ← AI, attribut, drops
│   └── client/
│       └── DaystalkerRenderer.java  ← Rendering
└── init/
    └── ModEntities.java        ← Entity-registrering
```

# 🐉 Dragon — Mod Guide

## Spawning
The dragon does not spawn naturally yet. Use this command to summon one:
```
/summon daystalker:dragon
```

---

## Step 1 — Taming
The dragon is wild when spawned. To tame it:

- Hold **Raw Cod** or **Raw Salmon** in your hand
- Right-click the dragon to feed it
- Repeat until **heart particles** appear — it is now tamed!

> 💡 It may take several fish before the dragon is tamed. Smoke particles mean it failed — keep trying.

---

## Step 2 — Saddling
Once tamed, you need a saddle to ride it:

- Craft or find a **Saddle** (found in chests or fishing)
- Right-click the dragon while holding the saddle
- You will hear a saddle-equip sound confirming it worked

---

## Step 3 — Riding & Flying
With the saddle equipped:

- **Right-click** the dragon to mount it
- Use normal movement controls to fly:

| Key | Action |
|-----|--------|
| W | Fly forward |
| A / D | Turn left / right |
| S | Fly backward |
| Space | Ascend (fly up) |
| Shift | Descend (fly down) |

- Press **Shift** to dismount

---

## Step 4 — Following you
When you dismount, the tamed dragon will follow you around.

To make it sit and stay in one place:
- Right-click it with an **empty hand** (without saddle equipped)
- Right-click again to make it follow you again

---

## Dragon Stats

| Property | Value |
|----------|-------|
| Health | 40 HP (20 hearts) |
| Flying speed | Fast |
| Walking speed | Medium |
| Tamed with | Raw Cod / Raw Salmon |
| Saddle required | Yes, to ride |
| Fall damage | None |

---

## Tips
- The dragon **does not take fall damage**, so landing from high up is safe
- Keep it fed and it will loyally follow you everywhere
- The dragon is passive — it will not attack mobs on its own in this version
- /place feature daystalker:bamboo_tower
- /give @s daystalker:lava_sword
- B = Blaze Rod, S = Stick (craftas som ett vanligt svärd fast med blaze rods) B B S

## Buildings
placed_features with value chance
- Extremt vanligt (Varje liten kulle): 10 till 30 (Bra för enstaka träd eller små stenar)
- Vanligt (Några stycken per biotop): 50 till 100 (Bra för ruiner eller brunnar)
- Mellan (Ungefär 1-2 per biotop): 200 till 350
- Sällsynt (Ungefär 1 per biotop): 450 till 600 (Perfekt för ditt Lava Tower!)
- Extremt sällsynt (Som ett Woodland Mansion): 1000+