package viewmodel;

import model.Model;
import view.AutoFishingViewController;

public class ViewModelFactory
{
  private MinecraftMiningScriptsViewModel minecraftMiningScriptsViewModel;
  private AutoClickViewModel autoclickViewModel;
private AutoFishingViewModel autoFishingViewModel;

  public ViewModelFactory(Model model){
    this.minecraftMiningScriptsViewModel=new MinecraftMiningScriptsViewModel(model);
    this.autoclickViewModel=new AutoClickViewModel(model);
    this.autoFishingViewModel=new AutoFishingViewModel(model);
  }

  public MinecraftMiningScriptsViewModel getMinecraftMiningScriptsViewModel()
  {
    return minecraftMiningScriptsViewModel;
  }

  public AutoClickViewModel getAutoclickViewModel()
  {
    return autoclickViewModel;
  }

  public AutoFishingViewModel getAutoFishingViewModel()
  {
    return autoFishingViewModel;
  }
}
