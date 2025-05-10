package viewmodel;

import model.Model;

public class ViewModelFactory
{
  private MinecraftMiningScriptsViewModel minecraftMiningScriptsViewModel;
  private AutoClickViewModel autoclickViewModel;

  public ViewModelFactory(Model model){
    this.minecraftMiningScriptsViewModel=new MinecraftMiningScriptsViewModel(model);
    this.autoclickViewModel=new AutoClickViewModel(model);
  }

  public MinecraftMiningScriptsViewModel getMinecraftMiningScriptsViewModel()
  {
    return minecraftMiningScriptsViewModel;
  }

  public AutoClickViewModel getAutoclickViewModel()
  {
    return autoclickViewModel;
  }


}
