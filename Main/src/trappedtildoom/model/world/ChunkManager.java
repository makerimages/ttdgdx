package trappedtildoom.model.world;

import com.sudoplay.joise.module.*;
import com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType;
import com.sudoplay.joise.module.ModuleFractal.FractalType;

public class ChunkManager {

    private final double SEMIRARE_DENSITY = 0.5;
    private final double RARE_DENSITY = 0.5;
    private final double RARE_GRADIENT_SCALE = 0.5;
    private final double DIRT_THRESHOLD = 0.5;
    private final double GROUND_YSCALE = 0.1;

    private Chunk activeChunk = new Chunk();

    public void initializeWorld() {
        //activeChunk.initialize(createTerrain2(createTerrain()));
        activeChunk.initialize(createBlockTypes(createTerrain()));
    }

    public Chunk getActiveChunk() {
        return activeChunk;
    }

    private Module createLowlands(Module groundGradient) {
        ModuleFractal shapeFractal = new ModuleFractal(FractalType.FBM, BasisType.GRADIENT, InterpolationType.QUINTIC);
        shapeFractal.setNumOctaves(2);
        shapeFractal.setFrequency(1);

        ModuleAutoCorrect autoCorrect = new ModuleAutoCorrect(0, 1);
        autoCorrect.setSource(shapeFractal);

        ModuleScaleOffset scale = new ModuleScaleOffset();
        scale.setSource(autoCorrect);
        scale.setSource(shapeFractal);
        scale.setScale(0.2);
        scale.setOffset(-0.25);

        ModuleScaleDomain scaleY = new ModuleScaleDomain();
        scaleY.setSource(scale);
        scaleY.setScaleY(0);

        ModuleTranslateDomain terrain = new ModuleTranslateDomain();
        terrain.setSource(groundGradient);
        terrain.setAxisYSource(scaleY);

        return terrain;
    }

    private Module createHighlands(Module groundGradient) {
        ModuleFractal shapeFractal = new ModuleFractal(FractalType.RIDGEMULTI, BasisType.GRADIENT, InterpolationType.QUINTIC);
        shapeFractal.setNumOctaves(2);
        shapeFractal.setFrequency(2);

        ModuleAutoCorrect autoCorrect = new ModuleAutoCorrect(0, 1);
        autoCorrect.setSource(shapeFractal);

        ModuleScaleOffset scale = new ModuleScaleOffset();
        scale.setSource(autoCorrect);
        scale.setSource(shapeFractal);
        scale.setScale(0.45);
        scale.setOffset(0);

        ModuleScaleDomain scaleY = new ModuleScaleDomain();
        scaleY.setSource(scale);
        scaleY.setScaleY(0);

        ModuleTranslateDomain terrain = new ModuleTranslateDomain();
        terrain.setSource(groundGradient);
        terrain.setAxisYSource(scaleY);

        return terrain;
    }

    private Module createMountains(Module groundGradient) {
        ModuleFractal shapeFractal = new ModuleFractal(FractalType.BILLOW, BasisType.GRADIENT, InterpolationType.QUINTIC);
        shapeFractal.setNumOctaves(4);
        shapeFractal.setFrequency(1);

        ModuleAutoCorrect autoCorrect = new ModuleAutoCorrect(0, 1);
        autoCorrect.setSource(shapeFractal);

        ModuleScaleOffset scale = new ModuleScaleOffset();
        scale.setSource(autoCorrect);
        scale.setSource(shapeFractal);
        scale.setScale(0.75);
        scale.setOffset(0.25);

        ModuleScaleDomain scaleY = new ModuleScaleDomain();
        scaleY.setSource(scale);
        //scaleY.setScaleY(0.1);
        scaleY.setScaleY(0);

        ModuleTranslateDomain terrain = new ModuleTranslateDomain();
        terrain.setSource(groundGradient);
        terrain.setAxisYSource(scaleY);

        return terrain;
    }

    private Module createTerrain() {
        ModuleGradient groundGradient = new ModuleGradient();
        groundGradient.setGradient(0, 0, 0, 1);

        ModuleFractal terrainType = new ModuleFractal(FractalType.FBM, BasisType.GRADIENT, InterpolationType.QUINTIC);
        terrainType.setNumOctaves(3);
        terrainType.setFrequency(0.5);

        ModuleAutoCorrect autoCorrect = new ModuleAutoCorrect(0, 1);
        autoCorrect.setSource(terrainType);

        ModuleCache terrainTypeCache = new ModuleCache();
        terrainTypeCache.setSource(autoCorrect);
        terrainTypeCache.setSource(terrainType);

        Module lowlandTerrain = createLowlands(groundGradient);
        Module highlandTerrain = createHighlands(groundGradient);
        Module mountainTerrain = createMountains(groundGradient);

        ModuleSelect highlandMountainSelect = new ModuleSelect();
        highlandMountainSelect.setLowSource(highlandTerrain);
        highlandMountainSelect.setHighSource(mountainTerrain);
        highlandMountainSelect.setControlSource(terrainTypeCache);
        highlandMountainSelect.setThreshold(0.30);
        highlandMountainSelect.setFalloff(0.15);

        ModuleSelect highlandLowlandSelect = new ModuleSelect();
        highlandLowlandSelect.setLowSource(lowlandTerrain);
        highlandLowlandSelect.setHighSource(highlandMountainSelect);
        highlandLowlandSelect.setControlSource(terrainTypeCache);
        highlandLowlandSelect.setThreshold(0.40);
        highlandLowlandSelect.setFalloff(0.15);

        ModuleSelect groundSelect = new ModuleSelect();
        groundSelect.setLowSource(0);
        groundSelect.setHighSource(1);
        groundSelect.setThreshold(0.5);
        groundSelect.setControlSource(highlandLowlandSelect);

        return groundSelect;
    }

    private Module createTerrain2(Module groundBase) {
        /*
        ModuleGradient groundGradient = new ModuleGradient();
        groundGradient.setGradient(0, 0, 0, 1);

        ModuleFractal groundShape = new ModuleFractal(FractalType.FBM, BasisType.GRADIENT, InterpolationType.QUINTIC);
        groundShape.setNumOctaves(2);
        groundShape.setFrequency(1.75);

        ModuleScaleDomain scaleY = new ModuleScaleDomain();
        scaleY.setSource(groundShape);
        scaleY.setScaleY(0.3);

        ModuleTranslateDomain groundTurbulence = new ModuleTranslateDomain();
        groundTurbulence.setSource(groundGradient);
        groundTurbulence.setAxisYSource(scaleY);

        ModuleSelect groundBase = new ModuleSelect();
        groundBase.setControlSource(groundTurbulence);
        groundBase.setLowSource(-1);
        groundBase.setHighSource(1);
        groundBase.setThreshold(0.2);
        groundBase.setFalloff(0);
        */

        ModuleFractal caveShape = new ModuleFractal(FractalType.RIDGEMULTI, BasisType.GRADIENT, InterpolationType.QUINTIC);
        caveShape.setNumOctaves(1);
        caveShape.setFrequency(2);

        ModuleSelect caveBase = new ModuleSelect();
        caveBase.setControlSource(caveShape);
        caveBase.setLowSource(0);
        caveBase.setHighSource(1);
        caveBase.setThreshold(0.7);
        caveBase.setFalloff(0);

        /*
        ModuleFractal caveShape2 = new ModuleFractal(FractalType.RIDGEMULTI, BasisType.GRADIENT, InterpolationType.QUINTIC);
        caveShape2.setNumOctaves(1);
        caveShape2.setFrequency(2);
        caveShape2.setSeed(100);

        ModuleSelect caveBase2 = new ModuleSelect();
        caveBase2.setControlSource(caveShape2);
        caveBase2.setLowSource(0);
        caveBase2.setHighSource(1);
        caveBase2.setThreshold(0.7);
        caveBase2.setFalloff(0);

        ModuleCombiner caveMultiplication = new ModuleCombiner(ModuleCombiner.CombinerType.MULT);
        caveMultiplication.setSource(0, caveBase1);
        caveMultiplication.setSource(1, caveBase2);
        */

        ModuleFractal caveTurbulenceX = new ModuleFractal(FractalType.FBM, BasisType.GRADIENT, InterpolationType.QUINTIC);
        caveTurbulenceX.setNumOctaves(3);
        caveTurbulenceX.setFrequency(3);
        caveTurbulenceX.setSeed(1001);

        ModuleFractal caveTurbulenceY = new ModuleFractal(FractalType.FBM, BasisType.GRADIENT, InterpolationType.QUINTIC);
        caveTurbulenceY.setNumOctaves(3);
        caveTurbulenceY.setFrequency(3);
        caveTurbulenceY.setSeed(10002);

        ModuleScaleDomain scaleCaveX = new ModuleScaleDomain();
        scaleCaveX.setSource(caveTurbulenceX);
        scaleCaveX.setScaleY(0.25);

        ModuleScaleDomain scaleCaveY = new ModuleScaleDomain();
        scaleCaveY.setSource(caveTurbulenceY);
        scaleCaveY.setScaleY(0.25);

        ModuleTranslateDomain caveTurbulence = new ModuleTranslateDomain();
        caveTurbulence.setSource(caveBase);
        caveTurbulence.setAxisXSource(scaleCaveX);
        caveTurbulence.setAxisYSource(scaleCaveY);

        ModuleScaleOffset caveInverter = new ModuleScaleOffset();
        caveInverter.setSource(caveTurbulence);
        caveInverter.setScale(-1);
        caveInverter.setOffset(1);

        ModuleCombiner groundCaveMultiplication = new ModuleCombiner(ModuleCombiner.CombinerType.MULT);
        groundCaveMultiplication.setSource(0, groundBase);
        groundCaveMultiplication.setSource(1, caveTurbulence);

        return groundCaveMultiplication;
    }

    private Module createBlockTypes(Module groundBase) {
        ModuleGradient mainGradient = new ModuleGradient();
        mainGradient.setGradient(0, 0, 0, 0.5);

        ModuleScaleOffset mainGradientRemap = new ModuleScaleOffset();
        mainGradientRemap.setSource(mainGradient);
        mainGradientRemap.setScale(0.5);
        mainGradientRemap.setOffset(0.5);

        ModuleFractal semiRareFBM = new ModuleFractal(FractalType.FBM, BasisType.GRADIENT, InterpolationType.QUINTIC);
        semiRareFBM.setNumOctaves(4);
        semiRareFBM.setFrequency(2);

        ModuleScaleOffset semiRareFBMRemap = new ModuleScaleOffset();
        semiRareFBMRemap.setSource(semiRareFBM);
        semiRareFBMRemap.setScale(0.5);
        semiRareFBMRemap.setOffset(0.5);

        ModuleSelect semiRareSelect = new ModuleSelect();
        semiRareSelect.setControlSource(semiRareFBMRemap);
        semiRareSelect.setLowSource(BlockType.Stone.blockId);
        semiRareSelect.setHighSource(BlockType.SemiRare.blockId);
        semiRareSelect.setThreshold(SEMIRARE_DENSITY);
        semiRareSelect.setFalloff(0);

        ModuleFractal rareFBM = new ModuleFractal(FractalType.FBM, BasisType.GRADIENT, InterpolationType.QUINTIC);
        rareFBM.setNumOctaves(3);
        rareFBM.setFrequency(3);

        ModuleScaleOffset rareFBMRemap = new ModuleScaleOffset();
        rareFBMRemap.setSource(rareFBM);
        rareFBMRemap.setScale(0.5);
        rareFBMRemap.setOffset(0.5);

        ModuleScaleOffset rareFBMScale = new ModuleScaleOffset();
        rareFBMScale.setSource(rareFBMRemap);
        rareFBMScale.setScale(RARE_GRADIENT_SCALE);
        rareFBMScale.setOffset(0);

        ModuleCombiner rareMult = new ModuleCombiner(ModuleCombiner.CombinerType.MULT);
        rareMult.setSource(0, rareFBMScale);
        rareMult.setSource(1, mainGradientRemap);

        ModuleScaleOffset rareMultScale = new ModuleScaleOffset();
        rareMultScale.setSource(rareMult);
        rareMultScale.setScale(RARE_DENSITY);
        rareMultScale.setOffset(0);

        ModuleSelect rareSelect = new ModuleSelect();
        rareSelect.setControlSource(rareMultScale);
        rareSelect.setLowSource(semiRareSelect);
        rareSelect.setHighSource(BlockType.Rare.blockId);
        rareSelect.setThreshold(0.5);
        rareSelect.setFalloff(0);

        ModuleSelect dirtStoneSelect = new ModuleSelect();
        dirtStoneSelect.setControlSource(mainGradientRemap);
        dirtStoneSelect.setLowSource(BlockType.Dirt.blockId);
        dirtStoneSelect.setHighSource(rareSelect);
        dirtStoneSelect.setThreshold(DIRT_THRESHOLD);
        dirtStoneSelect.setFalloff(0);

        ModuleSelect groundSelect = new ModuleSelect();
        groundSelect.setControlSource(mainGradientRemap);
        groundSelect.setLowSource(BlockType.Open.blockId);
        groundSelect.setHighSource(dirtStoneSelect);
        groundSelect.setThreshold(0.000001);
        groundSelect.setFalloff(0);

        /////////////////////////

        ModuleFractal groundShape = new ModuleFractal(FractalType.FBM, BasisType.GRADIENT, InterpolationType.QUINTIC);
        groundShape.setNumOctaves(4);
        groundShape.setFrequency(2);

        ModuleScaleDomain groundYScale = new ModuleScaleDomain();
        groundYScale.setSource(groundShape);
        groundYScale.setScaleY(GROUND_YSCALE);

        ModuleTranslateDomain groundTurbulence = new ModuleTranslateDomain();
        groundTurbulence.setSource(groundSelect);
        groundTurbulence.setAxisYSource(groundYScale);

        return groundTurbulence;
    }
}
