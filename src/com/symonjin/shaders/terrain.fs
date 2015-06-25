#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;

void main(void){
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);

	float nDot1 = dot(unitNormal, unitLightVector);
	float brightness = max(nDot1, 0.2);
	vec3 diffuse = brightness * lightColor;


	vec3 unitVectorToCamera = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLight = reflect(lightDirection, unitNormal);

	float specularFactor = dot(reflectedLight, unitVectorToCamera);
	specularFactor = max(specularFactor, 0.0);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedFactor * reflectivity * lightColor;

    //out_color = vec4(diffuse, 0.1) * texture(textureSampler, pass_textureCoords) + vec4(finalSpecular, 0.1);
    out_color = texture(textureSampler, pass_textureCoords);
}