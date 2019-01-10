# LAOP
## Ajouter un algorithme
Dans le package `org.lrima.network.algorithms`, ajouter un package du nom de votre algorithme
Dans le package, il faut créer deux nouvelles classes, une classe Model et une classe Network.
### La classe modèle
La classe modèle doit hériter de la classe générique `NeuralNetworkModel` et doit contenir une annotation de type `AlgorithmInformation`. Par exemple, pour l'algorithme fullyConnected, la déclaration doit être faite comme suit :  
```
@AlgorithmInformation(name="Fully Connected", description = "Fully connected network with one hidden layer containing 2 neurons. All neurons are connected to each neurons in the next layer.")
public class FullyConnectedNeuralModel extends NeuralNetworkModel<FullyConnectedNeuralNetwork> { ...
```
L'héritage demande de redéfinir deux méthodes :
- `getDefaultOptions()` modifie le LinkedHashMap `option` et assigne les valeurs utilisées par défaut par le système de neurone. Ces valeurs peuvent ensuite être modifiées dans l'application avant de partir l'animation.
- `getNeuralNetworkClass()` retourne l'objet classe de `NeuralNetwork` 
### La classe NeuralNetwork
La classe NeuralNetwork doit hériter de la classe `NeuralNetwork` et implémenter par l'interface serialisable, par exemple :  
```
public class FullyConnectedNeuralNetwork extends NeuralNetwork implements Serializable { 
```
Plusieurs fonctions ont besoin d'être redéfinies : 
- `init(ArrayList<NeuralNetworkTransmitter>, NeuralNetworkReceiver)` : initialise le neuralNetwork. Il faut appeler la fonction `super.init()` avec les bons paramètres pour être sûr que l'algorithme fonctionne bien 
- `crossOver(NeuralNetowk, NeuralNetwork)` : fonction qui permet au système de neurone d'apprendre. Elle retourne un nouveau système de neurone en fonction de deux autres en faisant un mélange des deux pour en obtenir un nouveau.
- `generationFinish()` : elle est appelée à la fin de chaque génération. Souvent utilisé pour muter ses connexions.
- `draw(Graphics2D)` : (optionnel) permet de visualiser graphiquement le système de neurone lorsqu'on clique sur une voiture pendant la simulation.
### Ajout de l'algorithme
Lorsque ces étapes sont faites, il faut ajouter le nouvel algorithme à la liste des algorithmes pris en charge. Ceci se fait en ajoutant la classe du model de notre algorithme dans la liste présente dans `AlgorithmManager` qui est dans `org.lrima.network.algorithms`
