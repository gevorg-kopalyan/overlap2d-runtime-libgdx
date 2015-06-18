/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.renderer.factory.component;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.components.sprite.AnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.legacy.data.FrameRange;
import com.uwsoft.editor.renderer.legacy.data.MainItemVO;
import com.uwsoft.editor.renderer.legacy.data.SpriteAnimationVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by azakhary on 5/22/2015.
 */
public class SpriteComponentFactory extends ComponentFactory {

    public SpriteComponentFactory(RayHandler rayHandler, World world, IResourceRetriever rm) {
        super(rayHandler, world, rm);
    }

    @Override
    public void createComponents(Entity root, Entity entity, MainItemVO vo) {
        createCommonComponents(entity, vo, EntityFactory.SPRITE_TYPE);
        createParentNodeComponent(root, entity);
        createNodeComponent(root, entity);
        createPhysicsComponents(entity, vo);
        createSpriteAnimationDataComponent(entity, (SpriteAnimationVO) vo);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
        DimensionsComponent component = new DimensionsComponent();
        component.height = 100;
        component.width = 100;

        entity.add(component);
        return component;
    }

    protected SpriteAnimationComponent createSpriteAnimationDataComponent(Entity entity, SpriteAnimationVO vo) {
        SpriteAnimationComponent spriteAnimationComponent = new SpriteAnimationComponent();
        spriteAnimationComponent.animationName = vo.animationName;
        spriteAnimationComponent.frameRangeMap = vo.frameRangeMap;
        spriteAnimationComponent.fps = vo.fps;
        spriteAnimationComponent.currentAnimation = vo.currentAnimation;
        spriteAnimationComponent.playMode = vo.playMode;

        Array<TextureAtlas.AtlasRegion> regions = rm.getSpriteAnimation(spriteAnimationComponent.animationName).getRegions();

        AnimationComponent animationComponent = new AnimationComponent();
        SpriteAnimationStateComponent stateComponent = new SpriteAnimationStateComponent(regions);

        if(spriteAnimationComponent.frameRangeMap.isEmpty()) {
            spriteAnimationComponent.frameRangeMap.put("Default", new FrameRange(0, regions.size-1));
            spriteAnimationComponent.currentAnimation = "Default";
        }

        stateComponent.set(spriteAnimationComponent);

        TextureRegionComponent textureRegionComponent = new TextureRegionComponent();
        textureRegionComponent.region = regions.get(0);
        
        entity.add(textureRegionComponent);
        entity.add(stateComponent);
        entity.add(animationComponent);
        entity.add(spriteAnimationComponent);

        return spriteAnimationComponent;
    }
}
